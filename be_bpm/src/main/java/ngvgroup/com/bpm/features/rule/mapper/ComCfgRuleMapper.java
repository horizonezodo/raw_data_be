package ngvgroup.com.bpm.features.rule.mapper;

import ngvgroup.com.bpm.features.rule.dto.ResponseRuleDto;
import ngvgroup.com.bpm.features.rule.dto.RuleDTO;
import ngvgroup.com.bpm.features.rule.model.ComCfgRule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComCfgRuleMapper {
    RuleDTO toDto(ComCfgRule r);

    ComCfgRule toEntity(RuleDTO dto);

    ResponseRuleDto mapToDto(ComCfgRule dto);
}
