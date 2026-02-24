package com.naas.admin_service.features.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.naas.admin_service.features.category.dto.CtgInfTitleDTO;
import com.naas.admin_service.features.category.model.CtgInfTitle;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgInfTitleMapper {
    CtgInfTitle toE (CtgInfTitleDTO dto);
    CtgInfTitleDTO toDTO (CtgInfTitle e);
    List<CtgInfTitleDTO> toListDTO(List<CtgInfTitle> lst);
    void updateEntityFromDto(CtgInfTitleDTO dto, @MappingTarget CtgInfTitle entity);
}
