package ngvgroup.com.bpmn.mapper.CtgCfgResourceMapping;

import ngvgroup.com.bpmn.dto.CtgCfgResourceMapping.ResourceMappingDto;
import ngvgroup.com.bpmn.model.CtgCfgResourceMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CtgCfgResourceMappingMapper {
    CtgCfgResourceMappingMapper INSTANCE = Mappers.getMapper(CtgCfgResourceMappingMapper.class);

    ResourceMappingDto toDto(CtgCfgResourceMapping entity);

    List<ResourceMappingDto> toDtoList(List<CtgCfgResourceMapping> entities);

    CtgCfgResourceMapping toEntity(ResourceMappingDto dto);
}