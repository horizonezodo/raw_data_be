package com.naas.admin_service.features.common.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.common.dto.FileUploadResponse;
import com.naas.admin_service.features.common.service.MinIOService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MinIOServiceImpl implements MinIOService {
    private final MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucket;
    @Value("${minio.public-url}")
    private String minioPublicUrl;

    public MinIOServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public FileUploadResponse uploadFile(String prefixPath, MultipartFile file) {
        try {
            checkAndCreateBucket(bucket);

            String fileName = file.getOriginalFilename();
            String objectName = prefixPath;
            if (objectName != null && !objectName.isEmpty()) {
                if (!objectName.endsWith("/")) {
                    objectName += "/";
                }
                objectName += fileName;
            } else {
                objectName = fileName;
            }

            String contentType = file.getContentType();
            if (contentType == null || contentType.isBlank()) {
                contentType = detectContentTypeByExtension(fileName);
            }

            log.info("Uploading file to MinIO - Bucket: {}, Object: {}, Size: {}, ContentType: {}",
                    bucket, objectName, file.getSize(), contentType);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(contentType)
                            .build());

            String fileUrl = minioPublicUrl + "/" + objectName;
            log.info("File uploaded successfully. URL: {}", fileUrl);

            return FileUploadResponse.builder()
                    .url(fileUrl)
                    .fileName(fileName)
                    .originalFileName(fileName)
                    .path(prefixPath)
                    .bucket(bucket)
                    .fileSize(file.getSize())
                    .contentType(contentType)
                    .uploadTime(LocalDateTime.now())
                    .message("Upload file thành công!")
                    .build();
        } catch (Exception e) {
            log.error("Error uploading file to MinIO: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Không thể upload file lên MinIO: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String pathFile) {
        try {
            String objectName = extractObjectName(pathFile);

            checkObjectExists(bucket, objectName);

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build());
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Không thể xóa file khỏi MinIO: " + e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String pathFile) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(pathFile)
                            .build());
            return stream.readAllBytes();
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.ERROR_DOWNLOAD_FILE);
        }
    }

    private void checkAndCreateBucket(String bucketName) throws BusinessException {
        try {
            log.info("Checking if bucket '{}' exists...", bucketName);
            boolean isExist = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());
            log.info("Bucket '{}' exists: {}", bucketName, isExist);

            if (!isExist) {
                log.info("Creating bucket '{}'...", bucketName);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket '{}' created successfully", bucketName);
            }
        } catch (Exception e) {
            log.error("Error checking/creating bucket '{}': {}", bucketName, e.getMessage(), e);
            throw new BusinessException(CommonErrorCode.BAD_REQUEST);
        }
    }

    private String detectContentTypeByExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        Map<String, String> mimeTypes = new HashMap<>();
        mimeTypes.put("doc", "application/msword");
        mimeTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("xls", "application/vnd.ms-excel");
        mimeTypes.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("ppt", "application/vnd.ms-powerpoint");
        mimeTypes.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

        return mimeTypes.getOrDefault(extension, "application/octet-stream");
    }

    private String extractObjectName(String pathFile) {
        String objectName = pathFile;
        if (objectName.startsWith(minioPublicUrl + "/")) {
            objectName = objectName.substring(minioPublicUrl.length() + 1);
        }
        return objectName;
    }

    private void checkObjectExists(String bucket, String objectName) throws BusinessException {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build());
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                throw new BusinessException(
                        CommonErrorCode.NOT_FOUND_FILE);
            }
            throw new BusinessException(
                    CommonErrorCode.ERROR_MINIO, e.errorResponse().message());

        } catch (Exception e) {
            throw new BusinessException(
                    CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
