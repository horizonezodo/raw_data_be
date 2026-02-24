package com.naas.admin_service.features.setting.service.impl;

import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.features.setting.dto.SecurityConfigDto;
import com.naas.admin_service.features.setting.service.KeyCloakService;

import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RealmRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyCloakServiceImpl implements KeyCloakService {
    @Value("${security.keycloak.realm}")
    private String realm;

    private final Keycloak keycloak;

    @Override
    public SecurityConfigDto getSecurityConfig() {
        RealmResource realmResource = keycloak.realm(realm);
        RealmRepresentation rep = realmResource.toRepresentation();
        SecurityConfigDto dto = new SecurityConfigDto();
        // Parse password policy
        String passwordPolicy = rep.getPasswordPolicy();
        if (passwordPolicy != null) {
            dto.setMinLength(parsePolicyInt(passwordPolicy, "length"));
            dto.setPasswordExpireDays(parsePolicyInt(passwordPolicy, "forceExpiredPasswordChange"));
            dto.setRequireLowerCase(passwordPolicy.contains("lowerCase"));
            dto.setRequireUpperCase(passwordPolicy.contains("upperCase"));
            dto.setRequireSpecialChar(passwordPolicy.contains("specialChars"));
            dto.setRequireDigit(passwordPolicy.contains("digits"));
            // Blacklist
            String blacklist = null;
            int idx = passwordPolicy.indexOf(Constant.PASSWORD_BLACK_LIST);
            if (idx != -1) {
                int start = idx + Constant.PASSWORD_BLACK_LIST.length();
                int end = passwordPolicy.indexOf(")", start);
                if (end != -1) {
                    blacklist = passwordPolicy.substring(start, end);
                }
            }
            dto.setPasswordBlacklist(blacklist);
        } else {
            dto.setMinLength(null);
            dto.setPasswordExpireDays(null);
            dto.setRequireLowerCase(null);
            dto.setRequireUpperCase(null);
            dto.setRequireSpecialChar(null);
            dto.setRequireDigit(null);
            dto.setPasswordBlacklist(null);
        }
        // Parse brute force protection settings
        dto.setLockOnFailure(rep.isBruteForceProtected());
        dto.setMaxFailedAttempts(rep.getFailureFactor());
        dto.setSessionTimeoutSeconds(rep.getSsoSessionIdleTimeout());
        return dto;
    }

    @Override
    public void updateSecurityConfig(SecurityConfigDto dto) {
        RealmResource realmResource = keycloak.realm(realm);
        RealmRepresentation rep = realmResource.toRepresentation();
        // Build password policy string
        List<String> rules = new ArrayList<>();
        if (dto.getMinLength() != null && dto.getMinLength() > 0)
            rules.add("length(" + dto.getMinLength() + ")");
        if (dto.getPasswordExpireDays() != null && dto.getPasswordExpireDays() > 0)
            rules.add("forceExpiredPasswordChange(" + dto.getPasswordExpireDays() + ")");
        if (Boolean.TRUE.equals(dto.getRequireLowerCase()))
            rules.add("lowerCase(1)");
        if (Boolean.TRUE.equals(dto.getRequireUpperCase()))
            rules.add("upperCase(1)");
        if (Boolean.TRUE.equals(dto.getRequireSpecialChar()))
            rules.add("specialChars(1)");
        if (Boolean.TRUE.equals(dto.getRequireDigit()))
            rules.add("digits(1)");
        if (dto.getPasswordBlacklist() != null && !dto.getPasswordBlacklist().trim().isEmpty())
            rules.add(Constant.PASSWORD_BLACK_LIST + dto.getPasswordBlacklist().trim() + ")");
        String passwordPolicy = String.join(" and ", rules);

        rep.setPasswordPolicy(passwordPolicy);
        rep.setBruteForceProtected(dto.getLockOnFailure());
        rep.setFailureFactor(dto.getMaxFailedAttempts());
        rep.setSsoSessionIdleTimeout(dto.getSessionTimeoutSeconds());
        realmResource.update(rep);
    }

    private Integer parsePolicyInt(String policy, String key) {
        try {
            int idx = policy.indexOf(key + "(");
            if (idx == -1)
                return null;
            int start = idx + key.length() + 1;
            int end = policy.indexOf(")", start);
            return Integer.parseInt(policy.substring(start, end));
        } catch (Exception e) {
            return null;
        }
    }
}
