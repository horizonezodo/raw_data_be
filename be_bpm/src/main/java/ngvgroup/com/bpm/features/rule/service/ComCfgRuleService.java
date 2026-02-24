package ngvgroup.com.bpm.features.rule.service;

import ngvgroup.com.bpm.features.rule.dto.InfUserDto;
import ngvgroup.com.bpm.features.rule.dto.ResponseRuleDto;
import ngvgroup.com.bpm.features.rule.dto.RuleDTO;
import ngvgroup.com.bpm.features.rule.dto.RuleExcelDto;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComCfgRuleService {
    RuleDTO getRule(String ruleCode);

    void createRule(RuleDTO input);

    void updateUserFromRule(String ruleCode, RuleDTO dto);

    Page<ResponseRuleDto> searchRule(String keyword, Pageable pageable);

    List<RuleExcelDto> exportToExcel(RuleDTO ruleDTO, Pageable pageable);

    void deleteRule(String ruleCode);

    Page<RuleDTO> getRules(RuleDTO dto, Pageable pageable);

    List<InfUserDto> listInfUsers(String orgCode, List<String> parentCode);

    List<InfUserDto> listUsername(List<String> listUsername);

    void checkRuleAccess(String userId, String orgCode, String processTypeCode);

    String addCandidates(DelegateTask delegateTask, String orgCode, String processTypeCode);

    String addCandidate(DelegateTask delegateTask, String orgCode, String processTypeCode);

    List<RuleDTO> getAllRule(String userId);

    Page<RuleDTO> searchAll(RuleDTO dto, Pageable pageable);

    List<InfUserDto> getAllRuleUserByRuleCode(String ruleCode);

    List<InfUserDto> listUser();

}
