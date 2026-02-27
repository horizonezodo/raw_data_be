package com.naas.admin_service.features.common.service;

import org.springframework.web.multipart.MultipartFile;

import com.naas.admin_service.features.common.dto.FileUploadResponse;

public interface MinIOService {
    FileUploadResponse uploadFile(String prefixPath, MultipartFile file);

    void deleteFile(String pathFile);

    byte[] downloadFile(String pathFile);

    void uploadFileWithObjectName(String objectName, MultipartFile file);
}
