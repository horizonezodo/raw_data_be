package com.ngvgroup.bpm.core.autoconfigure;

import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import com.ngvgroup.bpm.core.security.UsernameNormalizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@AutoConfiguration
@ConditionalOnProperty(prefix="multitenancy", name="enabled", havingValue="true")
public class BpmCoreMultitenantUsernameNormalizerAutoConfiguration {

    /**
     * Multi-tenant normalizer:
     * preferred_username = "abc.tenantA" -> "abc" (dựa theo TenantContext tenantId)
     */
    @Bean
    @Primary
    public UsernameNormalizer usernameNormalizer(MultitenancyProperties props) {
        return preferredUsername -> {
            if (preferredUsername == null) return null;

            String tenantId = TenantContext.getTenantId();
            if (tenantId == null || tenantId.isBlank()) return preferredUsername;

            String suffix = "." + tenantId.toLowerCase();
            if (preferredUsername.endsWith(suffix)) {
                return preferredUsername.substring(0, preferredUsername.length() - suffix.length());
            }
            return preferredUsername;
        };
    }
}