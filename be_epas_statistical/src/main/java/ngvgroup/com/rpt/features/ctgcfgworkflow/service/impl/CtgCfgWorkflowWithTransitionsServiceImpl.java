package ngvgroup.com.rpt.features.ctgcfgworkflow.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.common.dto.CommonResponse;
import ngvgroup.com.rpt.features.common.repository.ComCfgCommonRepository;
import ngvgroup.com.rpt.features.ctgcfgtransition.model.CtgCfgTransitionCond;
import ngvgroup.com.rpt.features.ctgcfgtransition.model.CtgCfgTransitionPostFunc;
import ngvgroup.com.rpt.features.ctgcfgtransition.repository.CtgCfgTransitionCondRepository;
import ngvgroup.com.rpt.features.ctgcfgtransition.repository.CtgCfgTransitionPostFuncRepository;
import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.CtgCfgWorkflowWithTransitionsDto;
import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.TransitionConditionDto;
import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.TransitionPostFunctionDto;
import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.WorkflowTransitionDto;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.CtgCfgWorkflow;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.WorkflowTransition;
import ngvgroup.com.rpt.features.ctgcfgworkflow.repository.CtgCfgWorkflowRepository;
import ngvgroup.com.rpt.features.ctgcfgworkflow.repository.CtgCfgWorkflowTransitionRepository;
import ngvgroup.com.rpt.features.ctgcfgworkflow.service.CtgCfgWorkflowWithTransitionsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ngvgroup.com.rpt.core.constant.VariableConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CtgCfgWorkflowWithTransitionsServiceImpl implements CtgCfgWorkflowWithTransitionsService {

    private final CtgCfgWorkflowRepository workflowRepository;
    private final CtgCfgWorkflowTransitionRepository transitionRepository;
    private final CtgCfgTransitionCondRepository conditionRepository;
    private final CtgCfgTransitionPostFuncRepository postFuncRepository;
    private final ComCfgCommonRepository commonRepository;

    @Override
    @Transactional
    public CtgCfgWorkflowWithTransitionsDto createWorkflowWithTransitions(CtgCfgWorkflowWithTransitionsDto dto) {
        log.info(CREATING_WORKFLOW_LOG, dto.getWorkflowCode());

        // Kiểm tra mã quy trình đã tồn tại chưa
        if (workflowRepository.existsByWorkflowCodeAndIsDelete(dto.getWorkflowCode(), 0)) {
            throw new BusinessException(StatisticalErrorCode.WORKFLOW_CODE_CONFLICT);
        }

        // 1. Tạo workflow chính
        CtgCfgWorkflow workflow = new CtgCfgWorkflow();
        workflow.setWorkflowCode(dto.getWorkflowCode());
        workflow.setWorkflowName(dto.getWorkflowName());
        workflow.setInitialStatusCode(dto.getInitialStatusCode());
        workflow.setVersionNo(dto.getVersionNo() != null ? dto.getVersionNo() : java.math.BigDecimal.valueOf(0.00));
        workflow.setRecordStatus(DD);
        workflow.setDescription(dto.getDescription()); // Set description if provided

        CtgCfgWorkflow savedWorkflow = workflowRepository.save(workflow);
        log.info(CREATING_WORKFLOW_ID_LOG, savedWorkflow.getId());

        // 2. Tạo transitions và các thành phần liên quan
        if (dto.getTransitions() != null && !dto.getTransitions().isEmpty()) {
            for (WorkflowTransitionDto transitionDto : dto.getTransitions()) {
                createTransitionWithDetails(savedWorkflow.getWorkflowCode(), transitionDto);
            }
        }

        // 3. Trả về dữ liệu đã tạo
        return getWorkflowWithTransitions(savedWorkflow.getWorkflowCode());
    }

    @Override
    @Transactional
    public CtgCfgWorkflowWithTransitionsDto updateWorkflowWithTransitions(CtgCfgWorkflowWithTransitionsDto dto,
                                                                          String workflowCode) {
        log.info(UPDATE_WORKFLOW_LOG, workflowCode);

        // 1. Cập nhật workflow chính
        CtgCfgWorkflow existingWorkflow = workflowRepository.findByWorkflowCode(workflowCode)
                .orElseThrow(() -> new BusinessException(StatisticalErrorCode.NOT_FOUND, workflowCode));

        existingWorkflow.setWorkflowName(dto.getWorkflowName());
        existingWorkflow.setInitialStatusCode(dto.getInitialStatusCode());
        existingWorkflow
                .setVersionNo(dto.getVersionNo() != null ? dto.getVersionNo() : java.math.BigDecimal.valueOf(0.00));
        existingWorkflow.setRecordStatus(VariableConstants.DD); // Set default status

        CtgCfgWorkflow savedWorkflow = workflowRepository.save(existingWorkflow);
        log.info(UPDATE_WORKFLOW_ID_LOG, savedWorkflow.getId());

        // 2. Xóa tất cả transitions cũ và tạo mới
        deleteAllTransitionsForWorkflow(workflowCode);

        if (dto.getTransitions() != null && !dto.getTransitions().isEmpty()) {
            for (WorkflowTransitionDto transitionDto : dto.getTransitions()) {
                createTransitionWithDetails(workflowCode, transitionDto);
            }
        }

        // 3. Trả về dữ liệu đã cập nhật
        return getWorkflowWithTransitions(workflowCode);
    }

    @Override
    public CtgCfgWorkflowWithTransitionsDto getWorkflowWithTransitions(String workflowCode) {
        log.info(GETTING_WORKFLOW_LOG, workflowCode);

        // 1. Lấy workflow chính
        CtgCfgWorkflow workflow = workflowRepository.findByWorkflowCode(workflowCode)
                .orElseThrow(() -> new BusinessException(StatisticalErrorCode.NOT_FOUND, workflowCode));

        // 2. Lấy tất cả transitions
        List<WorkflowTransition> transitions = transitionRepository.findByWorkflowCode(workflowCode);

        // 3. Tạo DTO với đầy đủ thông tin
        CtgCfgWorkflowWithTransitionsDto result = new CtgCfgWorkflowWithTransitionsDto();
        result.setId(workflow.getId());
        result.setWorkflowCode(workflow.getWorkflowCode());
        result.setWorkflowName(workflow.getWorkflowName());
        result.setInitialStatusCode(workflow.getInitialStatusCode());
        result.setVersionNo(workflow.getVersionNo());
        result.setRecordStatus(workflow.getRecordStatus());

        // 4. Map transitions với conditions và post-functions
        List<WorkflowTransitionDto> transitionDtos = transitions.stream()
                .map(this::mapTransitionToDto)
                .toList();

        result.setTransitions(transitionDtos);

        return result;
    }

    @Override
    public Boolean existsByWorkflowCodeAndTransitionCodeAndIsDeleted(String workflowCode, String transitionCode) {
        return transitionRepository.existsByWorkflowCodeAndTransitionCodeAndIsDelete(workflowCode, transitionCode, 0);
    }

    private void createTransitionWithDetails(String workflowCode, WorkflowTransitionDto transitionDto) {
        // 1. Tạo transition
        WorkflowTransition transition = new WorkflowTransition();
        transition.setWorkflowCode(workflowCode);
        transition.setTransitionCode(transitionDto.getTransitionCode());
        transition.setTransitionName(transitionDto.getTransitionName());
        transition.setFromStatusCode(transitionDto.getFromStatusCode());
        transition.setFromStatusName(transitionDto.getFromStatusName());
        transition.setToStatusCode(transitionDto.getToStatusCode());
        transition.setToStatusName(transitionDto.getToStatusName());
        transition.setIsGlobal(transitionDto.getIsGlobal());
        transition.setSortNumber(transitionDto.getSortNumber());
        transition.setIsAllowComment(transitionDto.getIsAllowComment());
        transition.setIsAllowAttachment(transitionDto.getIsAllowAttachment());
        transition.setRecordStatus(DD); // Set default status

        WorkflowTransition savedTransition = transitionRepository.save(transition);
        log.info(TRANSACTION_CREATE_LOG, savedTransition.getTransitionCode());

        // 2. Tạo conditions
        if (transitionDto.getConditions() != null && !transitionDto.getConditions().isEmpty()) {
            for (TransitionConditionDto conditionDto : transitionDto.getConditions()) {
                CtgCfgTransitionCond condition = new CtgCfgTransitionCond();
                condition.setTransitionCode(transitionDto.getTransitionCode());
                condition.setConditionType(conditionDto.getConditionType());
                condition.setConditionNo(conditionDto.getConditionNo());
                condition.setIsMandatory(conditionDto.getIsMandatory());
                condition.setEntityScope(conditionDto.getEntityScope());
                condition.setErrorMessage(conditionDto.getErrorMessage());
                condition.setExpressionSql(conditionDto.getExpressionSql());
                condition.setRecordStatus(DD); // Set default status

                conditionRepository.save(condition);
                log.info(TRANSACTION_CREATE_LOG, transitionDto.getTransitionCode());
            }
        }

        // 3. Tạo post-functions
        if (transitionDto.getPostFunctions() != null && !transitionDto.getPostFunctions().isEmpty()) {
            for (TransitionPostFunctionDto postFuncDto : transitionDto.getPostFunctions()) {
                CtgCfgTransitionPostFunc postFunc = new CtgCfgTransitionPostFunc();
                postFunc.setTransitionCode(transitionDto.getTransitionCode());
                postFunc.setPostFunctionType(postFuncDto.getPostFunctionType());
                postFunc.setPostFunctionNo(postFuncDto.getPostFunctionNo());
                postFunc.setIsAsync(postFuncDto.getIsAsync());
                postFunc.setExpressionSql(postFuncDto.getExpressionSql());
                postFunc.setRecordStatus(DD); // Set default status

                postFuncRepository.save(postFunc);
                log.info(POST_FUNCTION_TRANSACTION_CREATE_LOG, transitionDto.getTransitionCode());
            }
        }
    }

    private WorkflowTransitionDto mapTransitionToDto(WorkflowTransition transition) {
        WorkflowTransitionDto dto = new WorkflowTransitionDto();
        dto.setTransitionCode(transition.getTransitionCode());
        dto.setTransitionName(transition.getTransitionName());
        dto.setFromStatusCode(transition.getFromStatusCode());
        dto.setFromStatusName(transition.getFromStatusName());
        dto.setToStatusCode(transition.getToStatusCode());
        dto.setToStatusName(transition.getToStatusName());
        dto.setIsGlobal(transition.getIsGlobal());
        dto.setSortNumber(transition.getSortNumber());
        dto.setIsAllowComment(transition.getIsAllowComment());
        dto.setIsAllowAttachment(transition.getIsAllowAttachment());
        dto.setWorkflowCode(transition.getWorkflowCode());

        // Lấy conditions
        List<CtgCfgTransitionCond> conditions = conditionRepository
                .findByTransitionCode(transition.getTransitionCode());
        List<TransitionConditionDto> conditionDtos = conditions.stream()
                .map(this::mapConditionToDto)
                .toList();
        dto.setConditions(conditionDtos);

        // Lấy post-functions
        List<CtgCfgTransitionPostFunc> postFuncs = postFuncRepository
                .findByTransitionCode(transition.getTransitionCode());
        List<TransitionPostFunctionDto> postFuncDtos = postFuncs.stream()
                .map(this::mapPostFunctionToDto)
                .toList();
        dto.setPostFunctions(postFuncDtos);

        return dto;
    }

    private TransitionConditionDto mapConditionToDto(CtgCfgTransitionCond condition) {
        TransitionConditionDto dto = new TransitionConditionDto();
        dto.setId(condition.getId());
        dto.setTransitionCode(condition.getTransitionCode());
        dto.setConditionType(condition.getConditionType());
        dto.setConditionTypeName(this.getCommonNameByCode(condition.getConditionType())); // Thêm tên loại điều kiện
        dto.setConditionNo(condition.getConditionNo());
        dto.setIsMandatory(condition.getIsMandatory());
        dto.setEntityScope(condition.getEntityScope());
        dto.setErrorMessage(condition.getErrorMessage());
        dto.setExpressionSql(condition.getExpressionSql());
        return dto;
    }

    private TransitionPostFunctionDto mapPostFunctionToDto(CtgCfgTransitionPostFunc postFunc) {
        TransitionPostFunctionDto dto = new TransitionPostFunctionDto();
        dto.setId(postFunc.getId());
        dto.setTransitionCode(postFunc.getTransitionCode());
        dto.setPostFunctionType(postFunc.getPostFunctionType());
        dto.setPostFunctionTypeName(this.getCommonNameByCode(postFunc.getPostFunctionType())); // Thêm tên loại hậu xử
        // lý
        dto.setPostFunctionNo(postFunc.getPostFunctionNo());
        dto.setIsAsync(postFunc.getIsAsync());
        dto.setExpressionSql(postFunc.getExpressionSql());
        return dto;
    }

    private void deleteAllTransitionsForWorkflow(String workflowCode) {
        // Xóa tất cả conditions và post-functions trước
        List<WorkflowTransition> transitions = transitionRepository.findByWorkflowCode(workflowCode);
        for (WorkflowTransition transition : transitions) {
            conditionRepository.deleteByTransitionCode(transition.getTransitionCode());
            postFuncRepository.deleteByTransitionCode(transition.getTransitionCode());
        }

        // Xóa tất cả transitions
        transitionRepository.deleteByWorkflowCode(workflowCode);
        log.info(DELETE_ALL_TRANSACTION_WORKFLOW_LOG, workflowCode);
    }

    /**
     * Hàm chung để lấy tên theo common code
     */
    private String getCommonNameByCode(String commonCode) {
        try {
            CommonResponse commonData = commonRepository.getCommonByCommonCode(commonCode);
            return commonData != null ? commonData.getCommonName() : commonCode;
        } catch (Exception e) {
            log.warn(ERROR_GETTING_COMMON_LOG, commonCode, e.getMessage());
            return commonCode; // Fallback to code if error
        }
    }
}
