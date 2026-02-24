package com.naas.admin_service.features.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileUploadResponse {
    private String url;
    private String fileName;
    private String originalFileName;
    private String path;
    private String bucket;
    private Long fileSize;
    private String contentType;
    private LocalDateTime uploadTime;
    private String message;
}
