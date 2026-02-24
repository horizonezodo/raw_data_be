package com.naas.admin_service.features.setting.service;

import org.springframework.data.domain.Pageable;

import com.naas.admin_service.features.setting.dto.AppConfigResponseDto;
import com.naas.admin_service.features.setting.dto.ComCfgSettingReqDto;
import com.naas.admin_service.features.setting.dto.ReportDto;
import com.naas.admin_service.core.excel.dto.response.PageResponse;

import java.util.List;
import java.util.Map;

public interface ComCfgSettingService {
    void createOrUpdateSettings(List<ComCfgSettingReqDto> requests);

    List<ComCfgSettingReqDto> getSettingsByCodes(List<String> settingCodes);

    List<ComCfgSettingReqDto> getLayoutPublicSettings();

    List<ComCfgSettingReqDto> getCaptchaSettings();

    PageResponse<ReportDto> getDashboardList(Pageable pageable);

    void deleteSetting(String settingCode);

    Map<String, Object> getConfigLog();

    AppConfigResponseDto getAppConfig();
}
