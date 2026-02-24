package com.naas.category_service.mapper.CtgInfPosition;

import com.naas.category_service.dto.CtgInfPosition.CtgInfPositionDTO;
import com.naas.category_service.model.CtgInfPosition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgInfPositionMapper {
    CtgInfPosition toE (CtgInfPositionDTO dto);
    CtgInfPositionDTO toDTO (CtgInfPosition e);
    List<CtgInfPositionDTO> toListDTO(List<CtgInfPosition> lst);
    void updateEntityFromDto(CtgInfPositionDTO dto, @MappingTarget CtgInfPosition entity);
}
