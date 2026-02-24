package com.naas.admin_service.features.common.mapper;

import com.naas.admin_service.features.common.dto.ComCfgBusModuleDto;
import com.naas.admin_service.features.common.model.ComCfgBusModule;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ComCfgBusModuleMapper extends BaseMapper<ComCfgBusModuleDto, ComCfgBusModule> {

}
