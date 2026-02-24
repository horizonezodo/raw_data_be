package com.naas.admin_service.features.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeycloakLoginResposeDto {
    private String url;
}
