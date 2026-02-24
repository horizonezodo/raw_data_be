package com.naas.admin_service.features.users.mapper;


import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.CtgCfgResourceMappingDTO;
import com.naas.admin_service.features.users.model.CtgCfgResourceMapping;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgCfgResourceMappingMapper extends BaseMapper<CtgCfgResourceMappingDTO, CtgCfgResourceMapping> {
}
