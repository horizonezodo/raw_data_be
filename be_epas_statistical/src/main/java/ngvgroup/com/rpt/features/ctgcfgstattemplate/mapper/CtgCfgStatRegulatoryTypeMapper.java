package ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatRegulatoryType;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgStatRegulatoryTypeMapper extends BaseMapper<CtgCfgStatRegulatoryTypeDTO, CtgCfgStatRegulatoryType> {
    CtgCfgStatRegulatoryTypeMapper INSTANCE = Mappers.getMapper(CtgCfgStatRegulatoryTypeMapper.class);
}
