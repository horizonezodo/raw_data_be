package ngvgroup.com.bpmn.service.impl;

import com.ngvgroup.bpm.core.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpmn.constant.StatusProcess;
import ngvgroup.com.bpmn.dto.RuleDTO.ExportExcelRuleDto;
import ngvgroup.com.bpmn.dto.RuleDTO.RuleDTO;
import ngvgroup.com.bpmn.dto.SearchDTO.SearchDTO;
import ngvgroup.com.bpmn.dto.UserDto.InfUserDto;
import ngvgroup.com.bpmn.mapper.ruleMapper.ComCfgRuleMapper;
import ngvgroup.com.bpmn.model.ComCfgRule;
import ngvgroup.com.bpmn.model.ComCfgRuleUser;
import ngvgroup.com.bpmn.repository.ComCfgRuleRepository;
import ngvgroup.com.bpmn.repository.ComCfgRuleUserRepositoy;
import ngvgroup.com.bpmn.service.ComCfgRuleService;

import ngvgroup.com.bpmn.utils.PageUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ngvgroup.bpm.core.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComCfgRuleServiceImpl implements ComCfgRuleService {

    private final ComCfgRuleRepository ruleRepo;

    private final ComCfgRuleUserRepositoy rule_userRepo;

    private final ExcelService excelService;

    private final ComCfgRuleMapper ruleMapper;

    @Override
    public RuleDTO getRule(String ruleCode)  {
        ComCfgRule comCfgRule = ruleRepo.findByRuleCodeAndIsDelete(ruleCode,0);
        if(comCfgRule != null){
            List<String> usersCode = rule_userRepo.findUserCodeByRuleCode(comCfgRule.getRuleCode());
            RuleDTO dto = ruleMapper.toDto(comCfgRule);
            dto.setUserIds(usersCode);
            return dto;
        }else{
            log.error("Error: Không tìm thấy rule với rule code {}", ruleCode);
            throw new BusinessException(NOT_FOUND, ruleCode);
        }
    }
    @Transactional
    @Override
    public void createRule(RuleDTO input) {
        String genRuleCode = String.join("_", input.getOrgCode(), input.getParentCode());
        if(ruleRepo.existsByRuleCodeAndIsDelete(genRuleCode,0)){
            log.error("Error: Trùng mã rule chia bài {}", genRuleCode);
            throw new BusinessException(BAD_REQUEST, "Mã rule chia bài đã tồn tại: " + genRuleCode);
        }
        ComCfgRule comCfgRule = new ComCfgRule(genRuleCode, input.getRuleName(),input.getParentCode(), input.getOrgCode());
        comCfgRule.setIsActive(1);
        comCfgRule.setRecordStatus(StatusProcess.APPROVAL);
        ruleRepo.save(comCfgRule);
        if(input.getUserIds() != null){
            List<ComCfgRuleUser> comCfgRule_users = input.getUserIds().stream()
                    .map(userId -> {
                        ComCfgRuleUser comCfgRule_user = new ComCfgRuleUser(genRuleCode, userId, input.getOrgCode());
                        comCfgRule_user.setIsActive(1);
                        comCfgRule_user.setRecordStatus(StatusProcess.APPROVAL);
                        return comCfgRule_user;
                    }).collect(Collectors.toList());
            rule_userRepo.saveAll(comCfgRule_users);
        }
    }

    @Transactional
    @Override
    public void updateUserFromRule(String ruleCode,RuleDTO dto) {
        rule_userRepo.softDeleteUsersNotInListUser(ruleCode,dto.getUserIds());
        rule_userRepo.restoreSoftDeleteUsers(ruleCode, dto.getUserIds());
        List<String> existingUserIds = rule_userRepo.findUserCodeByRuleCode(ruleCode);
        List<String> newUserIds = dto.getUserIds().stream()
                .filter(userId -> !existingUserIds.contains(userId))
                .toList();

        if(!newUserIds.isEmpty()){
            List<ComCfgRuleUser> listRuleUser = newUserIds.stream()
                    .map(userId -> {
                        ComCfgRuleUser comCfgRule_user = new ComCfgRuleUser(ruleCode, userId, dto.getOrgCode());
                        comCfgRule_user.setIsActive(1);
                        comCfgRule_user.setRecordStatus(StatusProcess.APPROVAL);
                        return comCfgRule_user;
                    }).collect(Collectors.toList());
            rule_userRepo.saveAll(listRuleUser);
        }
    }

    @Override
    public Page<RuleDTO> searchRule(SearchDTO searchDTO) {
        Pageable pageable = PageUtils.toPageable(searchDTO.getPageable());
        Page<ComCfgRule> rules = ruleRepo.searchRulesByName(searchDTO.getFilter(),pageable);
        return rules.map(ruleMapper::toDto);
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(RuleDTO ruleDTO, String fileName) {
        List<ComCfgRule> comCfgRules = ruleRepo.searchToExportExcel(
                ruleDTO.getRuleCode(),
                ruleDTO.getRuleName(),
                ruleDTO.getParentCode(),
                ruleDTO.getOrgCode());
        List<ExportExcelRuleDto> lst = ruleMapper.toListExDto(comCfgRules);
        return excelService.exportToExcel(lst, ruleDTO.getLabel(), ExportExcelRuleDto.class, fileName);
    }

    @Transactional
    @Override
    public void deleteRule(String ruleCode) {
        ComCfgRule rule = ruleRepo.findByRuleCodeAndIsDelete(ruleCode,0);
        if(rule != null){
           rule.setIsActive(0);
           rule.setIsDelete(1);
           ruleRepo.save(rule);
           List<ComCfgRuleUser> listRuleUser = rule_userRepo.findAllByRuleCode(ruleCode);
            listRuleUser.forEach(ruleUser -> {
                ruleUser.setIsActive(0);
                ruleUser.setIsDelete(1);
                rule_userRepo.save(ruleUser);
            });
        }else{
            log.error("Error: Không tìm thấy rule với rule code {}", ruleCode);
            throw new BusinessException(NOT_FOUND, ruleCode);
        }
    }
    @Transactional
    @Override
    public void updateParentCode(String ruleCode,RuleDTO ruleDTO) {
        ComCfgRule rule = ruleRepo.findByRuleCodeAndIsDelete(ruleCode,0);
        if(rule != null){
            String generateRuleCode = String.join("_", ruleDTO.getOrgCode(), ruleDTO.getParentCode());
            if(ruleRepo.existsByRuleCodeAndIsDelete(generateRuleCode,0)){
                log.error("Error: Trùng mã rule chia bài {}", generateRuleCode);
                throw new BusinessException(BAD_REQUEST, "Mã rule chia bài đã tồn tại: " + generateRuleCode );
            }
            List<ComCfgRuleUser> listRuleUser = rule_userRepo.findAllByRuleCode(rule.getRuleCode());
            listRuleUser.forEach(ruleUser -> {
                ruleUser.setRuleCode(generateRuleCode);
                rule_userRepo.save(ruleUser);
            });
            rule.setRuleCode(generateRuleCode);
            rule.setParentCode(ruleDTO.getParentCode());
            ruleRepo.save(rule);
        }else{
            log.error("Error: Không tìm thấy rule với rule code {}", ruleCode);
            throw new BusinessException(NOT_FOUND, ruleCode);
        }
    }

    @Override
    public Page<RuleDTO> getRules(RuleDTO dto) {
        ComCfgRule rule = ruleMapper.toEntity(dto);
        Pageable pageable = PageUtils.toPageable(dto.getPageable());
        Page<ComCfgRule> rules = ruleRepo.search(rule.getRuleCode(), rule.getRuleName(),rule.getParentCode(), rule.getOrgCode(),pageable);
        return rules.map(ruleMapper::toDto);
    }

    //Dùng sau này nếu có xóa 1 parentCode trong bảng common
    @Override
    public void deleteRuleByParentCode(String parentCode) {
        List<ComCfgRule> rules = ruleRepo.findByParentCode(parentCode);
        ruleRepo.deleteByParentCode(parentCode);
        rules.forEach(rule -> {
            rule_userRepo.deleteByRuleCode(rule.getRuleCode());
        });
    }

    @Override
    public List<RuleDTO> getAllRule(String userId) {
        List<String> ruleCodes = rule_userRepo.getAllRuleCode(userId);
        List<ComCfgRule> rules = ruleRepo.findAllRule(ruleCodes);
        return rules.stream()
                .map(ruleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RuleDTO> searchAll(RuleDTO dto) {
        Pageable pageable = PageUtils.toPageable(dto.getPageable());
        Page<ComCfgRule> rules = ruleRepo.searchAll(dto.getRuleCode(),dto.getRuleName(),dto.getParentCode(),dto.getOrgCode(),dto.getFilter(), pageable);
        return rules.map(ruleMapper::toDto);
    }

    @Override
    public List<InfUserDto> getAllRuleUserByRuleCode(String ruleCode) {
        List<String> userIds =  rule_userRepo.findAllByRuleCodeAndIsDelete(ruleCode, 0).stream()
                .map(ComCfgRuleUser::getUserId)
                .toList();
        return rule_userRepo.getAllUserByUserId(userIds);
    }
}
