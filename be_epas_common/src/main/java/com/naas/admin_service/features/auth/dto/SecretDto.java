package com.naas.admin_service.features.auth.dto;

import lombok.Data;

@Data
public class SecretDto {
    private String clientId;
    private String clientSecret;
}
