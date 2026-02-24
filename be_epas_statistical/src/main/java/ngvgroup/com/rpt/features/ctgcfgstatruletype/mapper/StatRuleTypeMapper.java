package ngvgroup.com.rpt.features.ctgcfgstatruletype.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.dto.StatRuleTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.model.StatRuleType;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StatRuleTypeMapper extends BaseMapper<StatRuleTypeDto, StatRuleType> {
    StatRuleTypeMapper INSTANCE = Mappers.getMapper(StatRuleTypeMapper.class);
}
