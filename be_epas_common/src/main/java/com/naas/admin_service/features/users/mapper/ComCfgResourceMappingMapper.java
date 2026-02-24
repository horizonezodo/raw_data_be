package com.naas.admin_service.features.users.mapper;

import com.naas.admin_service.features.users.model.CtgCfgResourceMapping;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMasterDtoV2;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComCfgResourceMappingMapper {
    CtgCfgResourceMapping toEntity(CtgCfgResourceMasterDtoV2 request);
}
