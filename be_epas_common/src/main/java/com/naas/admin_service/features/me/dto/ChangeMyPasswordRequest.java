package com.naas.admin_service.features.me.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeMyPasswordRequest {
    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
