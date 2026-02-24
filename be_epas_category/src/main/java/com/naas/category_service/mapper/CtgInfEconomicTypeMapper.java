package com.naas.category_service.mapper;

import com.naas.category_service.dto.CtgInfEconomicType.CtgInfEconomicTypeDto;
import com.naas.category_service.model.CtgInfEconomicType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CtgInfEconomicTypeMapper extends BaseMapper<CtgInfEconomicTypeDto, CtgInfEconomicType> {
    @Mapping(target = "economicTypeCode", ignore = true)
    void updateCtgInfEconomicTypeFromDto(CtgInfEconomicTypeDto dto, @MappingTarget CtgInfEconomicType entity);
}
