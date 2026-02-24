package com.naas.admin_service.features.setting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComCfgSettingReqDto {
    @NotBlank(message = "Setting code không được để trống")
    private String settingCode;

    private String settingValue;

    private String referenceCode;
}
