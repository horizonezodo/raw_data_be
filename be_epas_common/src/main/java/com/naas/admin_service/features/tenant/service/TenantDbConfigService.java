package com.naas.admin_service.features.tenant.service;

import com.naas.admin_service.features.tenant.dto.TenantRequestDto;
import com.naas.admin_service.features.tenant.dto.TenantResponseDto;
import com.naas.admin_service.features.tenant.model.TenantDbConfig;
import com.ngvgroup.bpm.core.persistence.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TenantDbConfigService extends BaseService<TenantDbConfig, TenantRequestDto> {
    Page<TenantResponseDto> search(String keyword, Pageable pageable);
    boolean isMultitenancyEnabled();
    boolean existsByTenantIdAndActive(String tenantId);
}