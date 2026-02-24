package com.nass.integration_service.mapper;

import com.nass.integration_service.dto.ComCfgParameterDto;
import com.nass.integration_service.model.ComCfgParameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComCfgParameterMapper extends EntityMapper<ComCfgParameterDto, ComCfgParameter>{

}
