package ngvgroup.com.bpmn.mapper.ruleMapper;

import ngvgroup.com.bpmn.dto.RuleDTO.Rule_DecisionDTO;
import ngvgroup.com.bpmn.model.ComCfgRuleDecision;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComCfgRuleDecisionMapper {
    Rule_DecisionDTO toDto(ComCfgRuleDecision rd);
    ComCfgRuleDecision toEntity(Rule_DecisionDTO dto);
}
