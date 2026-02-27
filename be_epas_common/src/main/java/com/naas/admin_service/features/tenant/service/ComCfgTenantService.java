package com.naas.admin_service.features.tenant.service;

import com.naas.admin_service.features.tenant.dto.ComCfgTenantRequestDto;
import com.naas.admin_service.features.tenant.dto.ComCfgTenantResponseDto;
import com.naas.admin_service.features.tenant.model.ComCfgTenant;
import com.ngvgroup.bpm.core.persistence.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComCfgTenantService extends BaseService<ComCfgTenant, ComCfgTenantRequestDto> {
    Page<ComCfgTenantResponseDto> search(String keyword, Pageable pageable);
    boolean isMultitenancyEnabled();
    boolean existsByTenantIdAndActive(String tenantId);
}