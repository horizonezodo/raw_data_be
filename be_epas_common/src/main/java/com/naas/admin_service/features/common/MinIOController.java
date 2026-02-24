package com.naas.admin_service.features.common;

import com.naas.admin_service.features.common.dto.FileUploadResponse;
import com.naas.admin_service.features.common.service.MinIOService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/minio")
public class MinIOController {

    private final MinIOService minIOService;

    public MinIOController(MinIOService minIOService) {
        this.minIOService = minIOService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseData<FileUploadResponse>> uploadFile(
            @RequestParam("prefixPath") String prefixPath,
            @RequestParam("file") MultipartFile file) {
        FileUploadResponse response = minIOService.uploadFile(prefixPath, file);
        return ResponseData.okEntity(response);
    }
}
