package com.naas.admin_service.features.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.naas.admin_service.features.category.dto.CtgInfPositionDTO;
import com.naas.admin_service.features.category.model.CtgInfPosition;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgInfPositionMapper {
    CtgInfPosition toE (CtgInfPositionDTO dto);
    CtgInfPositionDTO toDTO (CtgInfPosition e);
    List<CtgInfPositionDTO> toListDTO(List<CtgInfPosition> lst);
    void updateEntityFromDto(CtgInfPositionDTO dto, @MappingTarget CtgInfPosition entity);
}
