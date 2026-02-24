package com.naas.admin_service.features.setting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppConfigResponseDto {
    /**
     * Layout settings từ API layout-public
     */
    private List<ComCfgSettingReqDto> layoutSettings;

    /**
     * Trạng thái bật/tắt CAPTCHA
     */
    private Boolean captchaEnabled;

    /**
     * Trạng thái bật/tắt Multitenancy
     */
    private Boolean multitenancyEnabled;
}
