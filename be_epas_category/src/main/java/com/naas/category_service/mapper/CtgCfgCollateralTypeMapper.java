package com.naas.category_service.mapper;

import com.naas.category_service.dto.CtgCfgCollateralType.CtgCfgCollateralTypeDto;
import com.naas.category_service.model.CtgCfgCollateralType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CtgCfgCollateralTypeMapper extends BaseMapper<CtgCfgCollateralTypeDto, CtgCfgCollateralType>{

    @Mapping(target = "collateralTypeCode", ignore = true)
    void updateCtgCfgCollateralTypeFromDto(CtgCfgCollateralTypeDto dto, @MappingTarget CtgCfgCollateralType entity);
}
