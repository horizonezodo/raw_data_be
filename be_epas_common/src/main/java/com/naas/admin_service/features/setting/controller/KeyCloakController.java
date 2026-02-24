package com.naas.admin_service.features.setting.controller;

import com.naas.admin_service.features.setting.dto.SecurityConfigDto;
import com.naas.admin_service.features.setting.service.KeyCloakService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/keycloak")
@RequiredArgsConstructor
@Slf4j
@Validated
public class KeyCloakController {
    private final KeyCloakService keyCloakService;

    @GetMapping("/security-config")
    public ResponseEntity<ResponseData<SecurityConfigDto>> getSecurityConfig() {
        SecurityConfigDto config = keyCloakService.getSecurityConfig();
        return ResponseData.okEntity(config);
    }

    @PutMapping("/security-config")
    public ResponseEntity<ResponseData<Void>> updateSecurityConfig(@Valid @RequestBody SecurityConfigDto dto) {
        keyCloakService.updateSecurityConfig(dto);
        return ResponseData.okEntity(null);
    }

}
