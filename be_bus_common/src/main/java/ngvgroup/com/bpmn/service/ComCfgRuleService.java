package ngvgroup.com.bpmn.service;

import ngvgroup.com.bpmn.dto.RuleDTO.RuleDTO;
import ngvgroup.com.bpmn.dto.SearchDTO.SearchDTO;
import ngvgroup.com.bpmn.dto.UserDto.InfUserDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ComCfgRuleService {
    RuleDTO getRule(String ruleCode);
    void createRule(RuleDTO input) ;
    void updateUserFromRule(String ruleCode,RuleDTO dto);
    Page<RuleDTO> searchRule(SearchDTO searchDTO);
    ResponseEntity<ByteArrayResource> exportToExcel(RuleDTO ruleDTO, String fileName);
    void deleteRule(String ruleCode);
    void updateParentCode(String ruleCode,RuleDTO ruleDTO) ;
    Page<RuleDTO> getRules(RuleDTO dto);
    void deleteRuleByParentCode(String parentCode);
    List<RuleDTO> getAllRule(String userId);
    Page<RuleDTO> searchAll(RuleDTO dto);
    List<InfUserDto> getAllRuleUserByRuleCode(String ruleCode);
}
