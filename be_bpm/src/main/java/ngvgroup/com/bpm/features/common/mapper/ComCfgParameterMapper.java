package ngvgroup.com.bpm.features.common.mapper;

import ngvgroup.com.bpm.core.base.dto.ComCfgParameterDto;
import ngvgroup.com.bpm.features.common.model.ComCfgParameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComCfgParameterMapper {
    ComCfgParameterDto toDto(ComCfgParameter entity);
}
