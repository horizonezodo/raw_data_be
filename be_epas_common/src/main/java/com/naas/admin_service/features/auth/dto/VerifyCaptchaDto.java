package com.naas.admin_service.features.auth.dto;

import lombok.Getter;

@Getter
public class VerifyCaptchaDto {
    private String code;
    private String token;
}
