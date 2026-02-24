package com.naas.admin_service.features.setting.dto;

import lombok.Data;
import jakarta.validation.constraints.Min;

@Data
public class SecurityConfigDto {
    private Boolean requireLowerCase;
    private Boolean requireDigit;
    private Boolean requireSpecialChar;
    private Boolean requireUpperCase;

    @Min(value = 1, message = "Minimum length must be at least 1")
    private Integer minLength;

    @Min(value = 0, message = "Expire days must be non-negative")
    private Integer passwordExpireDays;

    private String passwordBlacklist;

    private Boolean lockOnFailure;

    @Min(value = 1, message = "Maximum fail attempts must be at least 1")
    private Integer maxFailedAttempts;

    @Min(value = 1, message = "Session timeout must be at least 1 second")
    private Integer sessionTimeoutSeconds;
}
