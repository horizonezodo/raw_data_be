package ngvgroup.com.rpt.features.ctgcfgresource.mapper;

import ngvgroup.com.rpt.features.ctgcfgresource.dto.ResourceMappingDto;
import ngvgroup.com.rpt.features.ctgcfgresource.model.ComCfgResourceMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CtgCfgResourceMappingMapper {
    CtgCfgResourceMappingMapper INSTANCE = Mappers.getMapper(CtgCfgResourceMappingMapper.class);

    ResourceMappingDto toDto(ComCfgResourceMapping entity);

    List<ResourceMappingDto> toDtoList(List<ComCfgResourceMapping> entities);

    ComCfgResourceMapping toEntity(ResourceMappingDto dto);
}
