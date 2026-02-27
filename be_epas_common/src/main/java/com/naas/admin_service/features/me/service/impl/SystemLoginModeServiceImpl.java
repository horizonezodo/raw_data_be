package com.naas.admin_service.features.me.service.impl;

import com.naas.admin_service.core.contants.SettingCode;
import com.naas.admin_service.features.me.service.SystemLoginModeService;
import com.naas.admin_service.features.setting.repository.ComCfgSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemLoginModeServiceImpl implements SystemLoginModeService {

    private final ComCfgSettingRepository settingRepository;

    @Override
    public boolean isSsoOnly() {
        return settingRepository.findBySettingCode(SettingCode.LAYOUT.SSO)
                .map(s -> s.getSettingValue() == null ? "" : s.getSettingValue().trim())
                .map(v -> v.equalsIgnoreCase("1") || v.equalsIgnoreCase("true") || v.equalsIgnoreCase("y"))
                .orElse(false);
    }
}
