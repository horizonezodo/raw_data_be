package com.naas.admin_service.features.setting.service;

import com.naas.admin_service.features.setting.dto.SecurityConfigDto;

public interface KeyCloakService {
    SecurityConfigDto getSecurityConfig();

    void updateSecurityConfig(SecurityConfigDto dto);
}
