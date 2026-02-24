package com.naas.admin_service.features.setting.mapper;

import com.naas.admin_service.features.setting.dto.ComCfgParameterDto;
import com.naas.admin_service.features.setting.model.ComCfgParameter;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ComCfgParameterMapper extends BaseMapper<ComCfgParameterDto, ComCfgParameter> {

    @Mapping(target = "paramCode", ignore = true)
    void updateEntity(ComCfgParameterDto dto, @MappingTarget ComCfgParameter entity);

    List<ComCfgParameterDto> mapToDTO(List<ComCfgParameter> lst);
}
