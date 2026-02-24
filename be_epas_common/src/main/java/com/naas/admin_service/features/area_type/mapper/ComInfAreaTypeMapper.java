package com.naas.admin_service.features.area_type.mapper;

import com.naas.admin_service.features.area_type.dto.ComInfAreaTypeDto;
import com.naas.admin_service.features.area_type.model.ComInfAreaType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComInfAreaTypeMapper {
    ComInfAreaTypeDto toDto(ComInfAreaType comInfAreaType);
}
