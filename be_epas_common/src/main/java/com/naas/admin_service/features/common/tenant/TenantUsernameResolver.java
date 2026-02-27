package com.naas.admin_service.features.common.tenant;

import org.springframework.stereotype.Component;

@Component
public class TenantUsernameResolver {

    public String effectiveUsername(String rawUsername, boolean multitenancyEnabled, String tenantId) {
        if (rawUsername == null) return null;

        String u = rawUsername.trim();
        if (u.isEmpty()) return u;

        if (!multitenancyEnabled) return u;
        if (tenantId == null || tenantId.isBlank()) return u;

        String suffix = "." + tenantId.trim();
        if (u.toLowerCase().endsWith(suffix.toLowerCase())) return u;
        return u + suffix;
    }

    public String stripTenantSuffix(String username, boolean multitenancyEnabled, String tenantId) {
        if (username == null) return null;

        String u = username.trim();
        if (u.isEmpty()) return u;
        if (!multitenancyEnabled) return u;
        if (tenantId == null || tenantId.isBlank()) return u;

        String suffix = "." + tenantId.trim();
        if (u.toLowerCase().endsWith(suffix.toLowerCase())) {
            return u.substring(0, u.length() - suffix.length());
        }
        return u;
    }
}
