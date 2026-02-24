package com.naas.admin_service.features.area.mapper;

import com.naas.admin_service.features.area.dto.ComInfAreaRequestDto;
import com.naas.admin_service.features.area.model.ComInfArea;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ComInfAreaMapper {

    ComInfArea toEntity(ComInfAreaRequestDto request);

    ComInfAreaRequestDto toDto(ComInfArea comInfArea);

    @Mapping(target = "areaCode", ignore = true)
    void updateCtgComAreaFromRequest(ComInfAreaRequestDto dto, @MappingTarget ComInfArea entity);
}
