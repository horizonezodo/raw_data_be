package com.naas.admin_service.features.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.naas.admin_service.features.category.dto.CtgInfCreditInstDTO;
import com.naas.admin_service.features.category.model.CtgInfCreditInst;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgInfCreditInstMapper {
    CtgInfCreditInst toE (CtgInfCreditInstDTO dto);
    CtgInfCreditInstDTO toDTO (CtgInfCreditInst e);
    void updateEntityFromDto(CtgInfCreditInstDTO dto, @MappingTarget CtgInfCreditInst entity);
    List<CtgInfCreditInstDTO> toListDTO(List<CtgInfCreditInst> lst);
}
