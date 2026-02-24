package com.naas.category_service.mapper.CtgInfTitle;

import com.naas.category_service.dto.CtgInfTitle.CtgInfTitleDTO;
import com.naas.category_service.model.CtgInfTitle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgInfTitleMapper {
    CtgInfTitle toE (CtgInfTitleDTO dto);
    CtgInfTitleDTO toDTO (CtgInfTitle e);
    List<CtgInfTitleDTO> toListDTO(List<CtgInfTitle> lst);
    void updateEntityFromDto(CtgInfTitleDTO dto, @MappingTarget CtgInfTitle entity);
}
