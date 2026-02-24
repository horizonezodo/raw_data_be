package com.naas.admin_service.features.common.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.Minio;
import com.naas.admin_service.features.common.dto.PublicImageResponse;
import com.naas.admin_service.features.common.service.PublicService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {
    private final MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucket;

    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpeg";

    @Override
    public PublicImageResponse getImage(String imagePath) {
        try {
            if (imagePath == null || imagePath.isBlank()) {
                throw new BusinessException(CommonErrorCode.NOT_FOUND);
            }

            // Normalize path:
            // 1) remove leading slashes
            // 2) remove trailing slashes
            // 3) collapse multiple slashes
            String normalizedPath = imagePath.trim()
                    .replaceAll("^/+", "")
                    .replaceAll("/+$", "")
                    .replaceAll("/{2,}", "/");

            String objectName = Minio.PUBLIC_FOLDER_NAME_IMAGES + "/" + normalizedPath;

            log.info("Getting image from MinIO - Bucket: {}, Object: {}", bucket, objectName);

            byte[] imageData;
            try (InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build())) {
                imageData = stream.readAllBytes();
            }

            String fileName = normalizedPath.substring(normalizedPath.lastIndexOf('/') + 1);
            String contentType = detectContentTypeByExtension(fileName);

            log.info("Image retrieved successfully - Size: {}, ContentType: {}", imageData.length, contentType);

            return PublicImageResponse.builder()
                    .imageData(imageData)
                    .contentType(contentType)
                    .build();

        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                log.warn("Image not found - Bucket: {}, Object: {}", bucket,
                        Minio.PUBLIC_FOLDER_NAME_IMAGES + "/" + imagePath);
                throw new BusinessException(CommonErrorCode.NOT_FOUND);
            }
            log.error("Error getting image from MinIO: {}", e.getMessage(), e);
            throw new BusinessException(CommonErrorCode.ERROR_MINIO, e.errorResponse().message());
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error getting image from MinIO: {}", e.getMessage(), e);
            throw new BusinessException(CommonErrorCode.ERROR_DOWNLOAD_FILE);
        }
    }

    private static final Map<String, String> MIME_TYPES = Map.of(
            "png",  "image/png",
            "jpeg", DEFAULT_IMAGE_CONTENT_TYPE,
            "jpg",  DEFAULT_IMAGE_CONTENT_TYPE,
            "gif",  "image/gif",
            "webp", "image/webp",
            "svg",  "image/svg+xml",
            "bmp",  "image/bmp",
            "ico",  "image/x-icon"
    );

    private String detectContentTypeByExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot < 0 || dot == fileName.length() - 1) {
            return DEFAULT_IMAGE_CONTENT_TYPE;
        }

        String ext = fileName.substring(dot + 1).toLowerCase();
        return MIME_TYPES.getOrDefault(ext, DEFAULT_IMAGE_CONTENT_TYPE);
    }

}
