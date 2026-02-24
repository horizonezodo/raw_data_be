package com.naas.admin_service.features.users.mapper;

import com.naas.admin_service.features.users.model.CtgCfgResourceMapping;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMasterDto;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.CtgCfgResourceMasterDtoV2;
import com.naas.admin_service.features.users.model.CtgCfgResourceMaster;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComCfgResourceMasterMapper {
    CtgCfgResourceMaster toEntity(CtgCfgResourceMasterDto ctgCfgResourceMasterDto);

    CtgCfgResourceMasterDto toDto(CtgCfgResourceMaster entity);

    CtgCfgResourceMasterDtoV2 toDtoV2(CtgCfgResourceMapping entity);
}
