package com.naas.admin_service.features.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.naas.admin_service.features.category.dto.CtgInfEconomicTypeDto;
import com.naas.admin_service.features.category.model.CtgInfEconomicType;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;

@Mapper(componentModel = "spring")
public interface CtgInfEconomicTypeMapper extends BaseMapper<CtgInfEconomicTypeDto, CtgInfEconomicType> {
    @Mapping(target = "economicTypeCode", ignore = true)
    void updateCtgInfEconomicTypeFromDto(CtgInfEconomicTypeDto dto, @MappingTarget CtgInfEconomicType entity);
}
