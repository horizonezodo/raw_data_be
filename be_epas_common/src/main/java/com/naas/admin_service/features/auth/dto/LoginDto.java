package com.naas.admin_service.features.auth.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String username;
    private String password;
    private String typeLogin;
    private VerifyCaptchaDto verifyCaptcha;
}
