package ngvgroup.com.bpmn.mapper.ruleMapper;

import ngvgroup.com.bpmn.dto.RuleDTO.Rule_UserDTO;
import ngvgroup.com.bpmn.model.ComCfgRuleUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComCfgRuleUserMapper {
    Rule_UserDTO toDto(ComCfgRuleUser ru);
    ComCfgRuleUser toEntity(Rule_UserDTO dto);
}
