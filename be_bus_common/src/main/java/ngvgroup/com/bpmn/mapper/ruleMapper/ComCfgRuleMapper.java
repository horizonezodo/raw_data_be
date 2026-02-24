package ngvgroup.com.bpmn.mapper.ruleMapper;

import ngvgroup.com.bpmn.dto.RuleDTO.ExportExcelRuleDto;
import ngvgroup.com.bpmn.dto.RuleDTO.RuleDTO;
import ngvgroup.com.bpmn.model.ComCfgRule;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComCfgRuleMapper {
    RuleDTO toDto(ComCfgRule r);
    ComCfgRule toEntity(RuleDTO dto);
    ExportExcelRuleDto toExDto(ComCfgRule r);
    List<ExportExcelRuleDto> toListExDto(List<ComCfgRule> lst);
}
