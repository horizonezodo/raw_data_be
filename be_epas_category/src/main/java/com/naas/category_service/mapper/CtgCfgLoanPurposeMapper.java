package com.naas.category_service.mapper;

import com.naas.category_service.dto.CtgCfgLoanPurpose.CtgCfgLoanPurposeDto;
import com.naas.category_service.model.CtgCfgLoanPurpose;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CtgCfgLoanPurposeMapper extends BaseMapper<CtgCfgLoanPurposeDto, CtgCfgLoanPurpose>{
    @Mapping(target = "purposeCode", ignore = true)
    void updateCtgCfgLoanPurposeFromDto(CtgCfgLoanPurposeDto dto, @MappingTarget CtgCfgLoanPurpose entity);
}
