package com.naas.admin_service.features.tenant.mapper;

import com.naas.admin_service.features.tenant.dto.ComCfgTenantRequestDto;
import com.naas.admin_service.features.tenant.model.ComCfgTenant;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ComCfgTenantMapper extends BaseMapper<ComCfgTenantRequestDto, ComCfgTenant> {
}