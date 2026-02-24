package com.naas.admin_service.features.category.mapper;

import org.mapstruct.Mapper;

import com.naas.admin_service.features.category.dto.HrmInfEmployeeDTO;
import com.naas.admin_service.features.category.model.HrmInfEmployee;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HrmInfEmployeeMapper {

    HrmInfEmployeeDTO toDto(HrmInfEmployee e);

    List<HrmInfEmployeeDTO> toListDto(List<HrmInfEmployee> lst);
}
