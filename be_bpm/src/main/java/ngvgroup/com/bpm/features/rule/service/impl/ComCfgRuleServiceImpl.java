package ngvgroup.com.bpm.features.rule.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.contants.BpmErrorCode;
import ngvgroup.com.bpm.core.contants.StatusConstants;
import ngvgroup.com.bpm.features.ctg_cfg_resource.model.CtgCfgResourceMapping;
import ngvgroup.com.bpm.features.ctg_cfg_resource.repository.CtgCfgResourceMappingRepository;
import ngvgroup.com.bpm.features.rule.dto.InfUserDto;
import ngvgroup.com.bpm.features.rule.dto.ResponseRuleDto;
import ngvgroup.com.bpm.features.rule.dto.RuleDTO;
import ngvgroup.com.bpm.features.rule.dto.RuleExcelDto;
import ngvgroup.com.bpm.features.rule.mapper.ComCfgRuleMapper;
import ngvgroup.com.bpm.features.rule.model.ComCfgRule;
import ngvgroup.com.bpm.features.rule.model.ComCfgRuleUser;
import ngvgroup.com.bpm.features.rule.openfeign.ComCfgRuleFeignClient;
import ngvgroup.com.bpm.features.rule.repository.ComCfgRuleRepository;
import ngvgroup.com.bpm.features.rule.repository.ComCfgRuleUserRepository;
import ngvgroup.com.bpm.features.rule.service.ComCfgRuleService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.IdentityLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComCfgRuleServiceImpl extends BaseStoredProcedureService implements ComCfgRuleService {

    private final ComCfgRuleRepository ruleRepo;
    private final ComCfgRuleUserRepository ruleUserRepo;

    private final ComCfgRuleMapper ruleMapper;
    private final ComCfgRuleRepository comCfgRuleRepository;

    private final ComCfgRuleFeignClient client;

    private final ProcessEngine processEngine;
    private final CtgCfgResourceMappingRepository ctgCfgResourceMappingRepository;

    @Override
    public RuleDTO getRule(String ruleCode) {
        ComCfgRule comCfgRule = ruleRepo.findByRuleCodeAndIsDelete(ruleCode, 0);
        if (comCfgRule != null) {
            List<String> usersCode = ruleUserRepo.findUserCodeByRuleCode(comCfgRule.getRuleCode());
            RuleDTO dto = ruleMapper.toDto(comCfgRule);
            dto.setUserIds(usersCode);
            return dto;
        } else {
            log.error("Error: Không tìm thấy rule với rule code {}", ruleCode);
            throw new BusinessException(ErrorCode.NOT_FOUND, "Mã chia bài");
        }
    }

    @Override
    public void createRule(RuleDTO input) {
        String genRuleCode = String.join("_", input.getOrgCode(), input.getParentCode());
        if (ruleRepo.existsByRuleCodeAndIsDelete(genRuleCode, 0)) {
            log.error("Error: Trùng mã rule chia bài {}", genRuleCode);
            throw new BusinessException(BpmErrorCode.RULE_CODE_ALREADY_EXISTS);
        }
        ComCfgRule comCfgRule = new ComCfgRule(genRuleCode, input.getRuleName(), input.getParentCode(), input.getOrgCode());
        comCfgRule.setIsActive(1);
        comCfgRule.setRecordStatus(StatusConstants.APPROVAL);
        ruleRepo.save(comCfgRule);
        if (input.getUserIds() != null) {
            List<ComCfgRuleUser> comCfgRuleUsers = input.getUserIds().stream()
                    .map(userId -> {
                        ComCfgRuleUser comCfgRuleUser = new ComCfgRuleUser(genRuleCode, userId, input.getOrgCode());
                        comCfgRuleUser.setIsActive(1);
                        comCfgRuleUser.setRecordStatus(StatusConstants.APPROVAL);
                        return comCfgRuleUser;
                    }).toList();
            ruleUserRepo.saveAll(comCfgRuleUsers);
        }
    }

    @Transactional
    @Override
    public void updateUserFromRule(String ruleCode, RuleDTO dto) {
        ruleUserRepo.softDeleteUsersNotInListUser(ruleCode, dto.getUserIds());
        ruleUserRepo.restoreSoftDeleteUsers(ruleCode, dto.getUserIds());
        List<String> existingUserIds = ruleUserRepo.findUserCodeByRuleCode(ruleCode);
        List<String> newUserIds = dto.getUserIds().stream()
                .filter(userId -> !existingUserIds.contains(userId))
                .toList();

        if (!newUserIds.isEmpty()) {
            List<ComCfgRuleUser> listRuleUser = newUserIds.stream()
                    .map(userId -> {
                        ComCfgRuleUser comCfgRuleUser = new ComCfgRuleUser(ruleCode, userId, dto.getOrgCode());
                        comCfgRuleUser.setIsActive(1);
                        comCfgRuleUser.setRecordStatus(StatusConstants.APPROVAL);
                        return comCfgRuleUser;
                    }).toList();
            ruleUserRepo.saveAll(listRuleUser);
        }
    }

    @Override
    public Page<ResponseRuleDto> searchRule(String keyword, Pageable pageable) {
        Page<ComCfgRule> rules = ruleRepo.searchRulesByName(keyword, pageable);
        return rules.map(ruleMapper::mapToDto);
    }

    @Override
    public List<RuleExcelDto> exportToExcel(RuleDTO ruleDTO, Pageable pageable) {
        Page<ComCfgRule> comCfgRules = ruleRepo.searchToExportExcel(
                ruleDTO.getRuleCode(),
                ruleDTO.getRuleName(),
                ruleDTO.getParentCode(),
                ruleDTO.getOrgCode(), pageable);
        List<RuleExcelDto> excelDtos = new ArrayList<>();
        for (ComCfgRule rule : comCfgRules.getContent()) {
            RuleExcelDto excelDto = new RuleExcelDto();
            excelDto.setRuleCode(rule.getRuleCode());
            excelDto.setRuleName(rule.getRuleName());
            excelDto.setParentCode(rule.getParentCode());
            excelDtos.add(excelDto);
        }
        return excelDtos;
    }

    @Override
    public void deleteRule(String ruleCode) {
        ComCfgRule rule = ruleRepo.findByRuleCodeAndIsDelete(ruleCode, 0);
        if (rule != null) {
            rule.setIsActive(0);
            rule.setIsDelete(1);
            ruleRepo.save(rule);
            List<ComCfgRuleUser> listRuleUser = ruleUserRepo.findAllByRuleCode(ruleCode);
            listRuleUser.forEach(ruleUser -> {
                ruleUser.setIsActive(0);
                ruleUser.setIsDelete(1);
                ruleUserRepo.save(ruleUser);
            });
        } else {
            log.error("Error: Không tìm thấy rule với rule code {}", ruleCode);
            throw new BusinessException(ErrorCode.NOT_FOUND, ruleCode);
        }
    }


    @Override
    public Page<RuleDTO> getRules(RuleDTO dto, Pageable pageable) {
        ComCfgRule rule = ruleMapper.toEntity(dto);
        Page<ComCfgRule> rules = ruleRepo.search(rule.getRuleCode(), rule.getRuleName(), rule.getParentCode(), rule.getOrgCode(), pageable);
        return rules.map(ruleMapper::toDto);
    }


    @Override
    public List<InfUserDto> listUsername(List<String> listUsername) {
        return getInfUserDtos(listUsername);
    }

    @Override
    public void checkRuleAccess(String userId, String orgCode, String processTypeCode) {
        if (!ruleUserRepo.existsByUserId(userId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        List<ComCfgRuleUser> ruleUserList = ruleUserRepo.findByUserIdAndOrgCodeAndIsDelete(userId, orgCode, 0);
        if (ruleUserList.isEmpty()) {
            throw new BusinessException(BpmErrorCode.OUT_OF_ORG);
        }

        List<CtgCfgResourceMapping> lst = ctgCfgResourceMappingRepository.findAllByUserIdAndIsDelete(userId, 0);
        boolean exists = lst.stream()
                .anyMatch(m -> m.getResourceCode().equals(processTypeCode));
        if (!exists) {
            throw new BusinessException(BpmErrorCode.CHECK_ACCESS_INCORRECT);
        }

    }


    @NotNull
    private List<InfUserDto> getInfUserDtos(List<String> listUsername) {
        var response = client.getUserInf(listUsername);

        if (response != null && response.getData() != null) {
            return response.getData();
        }
        return Collections.emptyList();

    }

    @NotNull
    private List<InfUserDto> getInfUserDtosV2(List<String> userIds) {
        var response = client.getUserInf2(userIds);

        if (response != null && response.getData() != null) {
            return response.getData();
        }
        return Collections.emptyList();

    }


    @Override
    public List<InfUserDto> listInfUsers(String orgCode, List<String> parentCode) {
        List<String> userIds = getUserId(orgCode, parentCode);

        return getInfUserDtosV2(userIds);
    }

    private List<String> getUserId(String orgCode, List<String> parentCode) {
        return this.comCfgRuleRepository
                .findAllByParentCodeAndOrgCode(parentCode, orgCode);
    }

    @Override
    public String addCandidates(DelegateTask delegateTask, String orgCode, String processTypeCode) {
        HistoryService historyService = processEngine.getHistoryService();
        String processInstanceId = delegateTask.getProcessInstanceId();
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();

        // Query lịch sử task đã hoàn thành
        List<HistoricTaskInstance> tasks = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(taskDefinitionKey)
                .finished()
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

        String assignee;
        if (tasks != null && !tasks.isEmpty() && tasks.get(0).getAssignee() != null) {
            assignee = tasks.get(0).getAssignee();
            delegateTask.addCandidateUser(assignee);
            log.info("Add candidate user from history: {}", assignee);
            return assignee;
        } else {
            List<String> groupIdsList = delegateTask.getCandidates().stream()
                    .map(IdentityLink::getGroupId)
                    .distinct()
                    .toList();

            List<InfUserDto> list = listInfUsers(orgCode, groupIdsList);
            List<String> listUsers = list.stream().map(InfUserDto::getUsername).toList();
            delegateTask.addCandidateUsers(listUsers);
            log.info("Add candidates users: " + listUsers);
            return String.join(",", listUsers);
        }
    }

    @Override
    public String addCandidate(DelegateTask delegateTask, String orgCode, String processTypeCode) {
        HistoryService historyService = processEngine.getHistoryService();
        String processInstanceId = delegateTask.getProcessInstanceId();
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();

        // Query lịch sử task đã hoàn thành
        List<HistoricTaskInstance> tasks = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey(taskDefinitionKey)
                .finished()
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

        String assignee;
        if (tasks != null && !tasks.isEmpty() && tasks.get(0).getAssignee() != null) {
            assignee = tasks.get(0).getAssignee();
            log.info("Add candidate user from history: {}", assignee);
        } else {
            assignee = getCurrentUserName();
            log.info("Add candidate user default: {}", assignee);
        }

        delegateTask.addCandidateUser(assignee);
        return assignee;
    }

    @Override
    public List<RuleDTO> getAllRule(String userId) {
        List<String> ruleCodes = ruleUserRepo.getAllRuleCode(userId);
        List<ComCfgRule> rules = ruleRepo.findAllRule(ruleCodes);
        return rules.stream()
                .map(ruleMapper::toDto)
                .toList();
    }

    @Override
    public Page<RuleDTO> searchAll(RuleDTO dto, Pageable pageable) {
        Page<ComCfgRule> rules = ruleRepo.searchAll(dto.getRuleCode(), dto.getRuleName(), dto.getParentCode(), dto.getOrgCode(), dto.getFilter(), pageable);
        return rules.map(ruleMapper::toDto);
    }

    @Override
    public List<InfUserDto> getAllRuleUserByRuleCode(String ruleCode) {
        List<String> userIds = ruleUserRepo.findAllByRuleCodeAndIsDelete(ruleCode, 0).stream()
                .map(ComCfgRuleUser::getUserId)
                .toList();
        return ruleUserRepo.getAllUserByUserId(userIds);
    }


    @Override
    public List<InfUserDto> listUser() {
        return getListUser();
    }

    @NotNull
    private List<InfUserDto> getListUser() {
        var response = client.listUser();

        if (response != null && response.getData() != null) {
            return response.getData();
        }
        return Collections.emptyList();

    }
}
