package com.naas.admin_service.features.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicImageResponse {
    private byte[] imageData;
    private String contentType;
}

