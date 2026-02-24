package com.ngvgroup.bpm.core.autoconfigure;

import com.ngvgroup.bpm.core.persistence.config.JdbcTenantDbConfigRegistry;
import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.security.BpmHttpSecurityCustomizer;
import com.ngvgroup.bpm.core.security.TenantJwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@AutoConfiguration
@ConditionalOnProperty(prefix = "multitenancy", name = "enabled", havingValue = "true")
@ConditionalOnBean(JdbcTenantDbConfigRegistry.class)
@EnableConfigurationProperties(MultitenancyProperties.class)
@RequiredArgsConstructor
public class BpmCoreSecurityMultitenantAutoConfiguration {

    private final MultitenancyProperties mtProps;

    /**
     * Tenant filter bean.
     * Must run AFTER JWT has been authenticated (so SecurityContext contains JwtAuthenticationToken)
     * and BEFORE authorization checks.
     */
    @Bean
    public TenantJwtFilter tenantJwtFilter(JdbcTenantDbConfigRegistry registry, RequestMatcher bpmPublicMatcher) {
        return new TenantJwtFilter(mtProps, registry, bpmPublicMatcher);
    }

    /**
     * Approach B: core owns SecurityFilterChain; multitenant contributes only a customizer.
     */
    @Bean
    public BpmHttpSecurityCustomizer multitenantSecurityCustomizer(TenantJwtFilter tenantJwtFilter) {
        return http -> http.addFilterAfter(tenantJwtFilter, BearerTokenAuthenticationFilter.class);
    }
}
