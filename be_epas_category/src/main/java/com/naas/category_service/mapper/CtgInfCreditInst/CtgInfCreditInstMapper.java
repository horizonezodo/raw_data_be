package com.naas.category_service.mapper.CtgInfCreditInst;

import com.naas.category_service.dto.CtgInfCreditInst.CtgInfCreditInstDTO;
import com.naas.category_service.model.CtgInfCreditInst;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgInfCreditInstMapper {
    CtgInfCreditInst toE (CtgInfCreditInstDTO dto);
    CtgInfCreditInstDTO toDTO (CtgInfCreditInst e);
    void updateEntityFromDto(CtgInfCreditInstDTO dto, @MappingTarget CtgInfCreditInst entity);
    List<CtgInfCreditInstDTO> toListDTO(List<CtgInfCreditInst> lst);
}
