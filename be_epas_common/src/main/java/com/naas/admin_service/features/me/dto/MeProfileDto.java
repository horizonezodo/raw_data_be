package com.naas.admin_service.features.me.dto;

import lombok.Data;

@Data
public class MeProfileDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String avatarUrl;

    /**
     * true = Layout_Login_SSO = 1 => ONLY login by SSO => hide edit/change-password
     */
    private boolean ssoOnly;
}
