package ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.dto.StatResponseDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.model.StatResponseDefine;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StatResponseDefineMapper extends BaseMapper<StatResponseDefineDto, StatResponseDefine> {
    StatResponseDefineMapper INSTANCE = Mappers.getMapper(StatResponseDefineMapper.class);
}
