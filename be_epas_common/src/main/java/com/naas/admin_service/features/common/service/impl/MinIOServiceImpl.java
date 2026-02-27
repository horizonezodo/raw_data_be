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

            String fileName = safeOriginalFilename(file);
            String objectName = buildObjectName(prefixPath, fileName);

            String contentType = resolveContentType(file, fileName);

            log.info("Uploading file to MinIO - Bucket: {}, Object: {}, Size: {}, ContentType: {}",
                    bucket, objectName, file.getSize(), contentType);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            String fileUrl = minioPublicUrl + "/" + objectName;

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

    /**
     * ✅ Upload theo objectName cố định (overwrite nếu đã tồn tại).
     * Ví dụ objectName: avatars/<userId>/avatar.png
     */
    @Override
    public void uploadFileWithObjectName(String objectName, MultipartFile file) {
        try {
            checkAndCreateBucket(bucket);

            String normalizedObjectName = normalizeObjectName(objectName);
            String fileName = safeOriginalFilename(file);
            String contentType = resolveContentType(file, fileName);

            log.info("Uploading file to MinIO - Bucket: {}, Object: {}, Size: {}, ContentType: {}",
                    bucket, normalizedObjectName, file.getSize(), contentType);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(normalizedObjectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            String fileUrl = minioPublicUrl + "/" + normalizedObjectName;

            FileUploadResponse.builder()
                    .url(fileUrl)
                    .fileName(fileName)
                    .originalFileName(fileName)
                    .path(normalizedObjectName)
                    .bucket(bucket)
                    .fileSize(file.getSize())
                    .contentType(contentType)
                    .uploadTime(LocalDateTime.now())
                    .message("Upload file thành công!")
                    .build();

        } catch (Exception e) {
            log.error("Error uploading file with objectName to MinIO: {}", e.getMessage(), e);
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
                            .build()
            );
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
            String objectName = extractObjectName(pathFile);

            try (InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            )) {
                return stream.readAllBytes();
            }
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.ERROR_DOWNLOAD_FILE);
        }
    }

    // =========================
    // Helpers
    // =========================

    private void checkAndCreateBucket(String bucketName) {
        try {
            boolean isExist = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error("Error checking/creating bucket '{}': {}", bucketName, e.getMessage(), e);
            throw new BusinessException(CommonErrorCode.BAD_REQUEST);
        }
    }

    private String safeOriginalFilename(MultipartFile file) {
        String fn = file != null ? file.getOriginalFilename() : null;
        if (fn == null || fn.isBlank()) {
            return "file";
        }
        return fn;
    }

    private String buildObjectName(String prefixPath, String fileName) {
        if (prefixPath == null || prefixPath.isBlank()) {
            return normalizeObjectName(fileName);
        }
        String p = prefixPath.trim();
        p = p.startsWith("/") ? p.substring(1) : p;
        p = p.endsWith("/") ? p.substring(0, p.length() - 1) : p;
        return p + "/" + fileName;
    }

    private String normalizeObjectName(String objectName) {
        if (objectName == null || objectName.isBlank()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "objectName không hợp lệ");
        }
        String o = objectName.trim();
        o = o.startsWith("/") ? o.substring(1) : o;
        // tránh // trong url/path
        while (o.contains("//")) o = o.replace("//", "/");
        return o;
    }

    private String resolveContentType(MultipartFile file, String fileName) {
        String contentType = file != null ? file.getContentType() : null;
        if (contentType == null || contentType.isBlank()) {
            contentType = detectContentTypeByExtension(fileName);
        }
        return contentType;
    }

    private String detectContentTypeByExtension(String fileName) {
        String ext = "bin";
        if (fileName != null) {
            int idx = fileName.lastIndexOf('.');
            if (idx >= 0 && idx < fileName.length() - 1) {
                ext = fileName.substring(idx + 1).toLowerCase();
            }
        }

        Map<String, String> mimeTypes = new HashMap<>();
        mimeTypes.put("doc", "application/msword");
        mimeTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        mimeTypes.put("pdf", "application/pdf");
        mimeTypes.put("xls", "application/vnd.ms-excel");
        mimeTypes.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("webp", "image/webp");
        mimeTypes.put("ppt", "application/vnd.ms-powerpoint");
        mimeTypes.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

        return mimeTypes.getOrDefault(ext, "application/octet-stream");
    }

    private String extractObjectName(String pathFile) {
        if (pathFile == null || pathFile.isBlank()) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND_FILE);
        }
        String objectName = pathFile.trim();
        String prefix = minioPublicUrl + "/";
        if (objectName.startsWith(prefix)) {
            objectName = objectName.substring(prefix.length());
        }
        objectName = objectName.startsWith("/") ? objectName.substring(1) : objectName;
        while (objectName.contains("//")) objectName = objectName.replace("//", "/");
        return objectName;
    }

    private void checkObjectExists(String bucket, String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                throw new BusinessException(CommonErrorCode.NOT_FOUND_FILE);
            }
            throw new BusinessException(CommonErrorCode.ERROR_MINIO, e.errorResponse().message());
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
