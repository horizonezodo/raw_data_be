package com.ngvgroup.bpm.core.security.oauth2.feign;

import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.persistence.config.OrganizationContext;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public record MultitenantFeignHeaderInterceptor(
        MultitenancyProperties multitenancyProperties) implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String tenant = TenantContext.getTenantId();
        if (tenant != null && !tenant.isBlank()) {
            template.header(multitenancyProperties.getTenantHeader(), tenant);
        }

        String org = OrganizationContext.getOrgAlias();
        if (org != null && !org.isBlank()
                && multitenancyProperties.getOrganizations() != null
                && multitenancyProperties.getOrganizations().getSelectHeader() != null) {

            template.header(multitenancyProperties.getOrganizations().getSelectHeader(), org);
        }
    }
}
