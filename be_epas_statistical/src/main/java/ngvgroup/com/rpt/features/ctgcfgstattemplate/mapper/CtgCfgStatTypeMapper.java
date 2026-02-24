package ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.CtgCfgStatTypeListDto;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatType;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgStatTypeMapper extends BaseMapper<CtgCfgStatTypeListDto, CtgCfgStatType> {
    CtgCfgStatTypeMapper INSTANCE = Mappers.getMapper(CtgCfgStatTypeMapper.class);
}
