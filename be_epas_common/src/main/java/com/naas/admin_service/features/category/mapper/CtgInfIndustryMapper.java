package com.naas.admin_service.features.category.mapper;

import com.naas.admin_service.features.category.dto.CtgInfIndustryDto;
import com.naas.admin_service.features.category.model.CtgInfIndustry;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CtgInfIndustryMapper extends BaseMapper<CtgInfIndustryDto, CtgInfIndustry>{
    @Mapping(target = "industryCode", ignore = true)
    void updateCtgInfIndustryFromDto(CtgInfIndustryDto dto, @MappingTarget CtgInfIndustry entity);
}
