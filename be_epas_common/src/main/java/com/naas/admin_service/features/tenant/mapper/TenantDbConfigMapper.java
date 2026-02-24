package com.naas.admin_service.features.tenant.mapper;

import com.naas.admin_service.features.tenant.dto.TenantRequestDto;
import com.naas.admin_service.features.tenant.model.TenantDbConfig;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TenantDbConfigMapper extends BaseMapper<TenantRequestDto, TenantDbConfig> {
}