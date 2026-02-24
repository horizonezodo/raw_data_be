package com.naas.category_service.mapper;

import com.naas.category_service.dto.CtgInfIndustry.CtgInfIndustryDto;
import com.naas.category_service.model.CtgInfIndustry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CtgInfIndustryMapper extends BaseMapper<CtgInfIndustryDto, CtgInfIndustry>{
    @Mapping(target = "industryCode", ignore = true)
    void updateCtgInfIndustryFromDto(CtgInfIndustryDto dto, @MappingTarget CtgInfIndustry entity);
}
