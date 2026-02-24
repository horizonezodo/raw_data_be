package ngvgroup.com.rpt.features.ctgcfgstatruledefine.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.StatRuleDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.model.StatRuleDefine;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StatRuleDefineMapper extends BaseMapper<StatRuleDefineDto, StatRuleDefine> {
    StatRuleDefineMapper INSTANCE = Mappers.getMapper(StatRuleDefineMapper.class);
}
