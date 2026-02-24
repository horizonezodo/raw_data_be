package ngvgroup.com.bpm.features.ctg_cfg_resource.mapper;


import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.bpm.features.ctg_cfg_resource.dto.ResourceMappingDto;
import ngvgroup.com.bpm.features.ctg_cfg_resource.model.CtgCfgResourceMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgCfgResourceMappingMapper extends BaseMapper<ResourceMappingDto, CtgCfgResourceMapping> {

    ResourceMappingDto toDto(CtgCfgResourceMapping entity);

    CtgCfgResourceMapping toEntity(ResourceMappingDto dto);
}