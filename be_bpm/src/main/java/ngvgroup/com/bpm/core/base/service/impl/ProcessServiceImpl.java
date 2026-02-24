package ngvgroup.com.bpm.core.base.service.impl;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.base.dto.ProcessData;
import ngvgroup.com.bpm.core.base.dto.TaskBpmData;
import ngvgroup.com.bpm.core.base.model.BpmTxnProcessInstance;
import ngvgroup.com.bpm.core.base.model.BpmTxnTaskComment;
import ngvgroup.com.bpm.core.base.repository.BpmTxnProcessInstanceRepository;
import ngvgroup.com.bpm.core.base.repository.BpmTxnTaskCommentRepository;
import ngvgroup.com.bpm.core.base.service.BpmService;
import ngvgroup.com.bpm.core.base.service.ProcessService;
import ngvgroup.com.bpm.core.contants.StatusConstants;
import ngvgroup.com.bpm.core.contants.VariableConstants;
import ngvgroup.com.bpm.core.utils.CamundaExtensionPropertiesUtil;
import ngvgroup.com.bpm.core.utils.VariablesUtil;
import ngvgroup.com.bpm.features.com_cfg_txn_content.repository.ComCfgTxnContentRepository;
import ngvgroup.com.bpm.features.common.repository.ComInfOrganizationRepository;
import ngvgroup.com.bpm.features.sla.repository.ComCfgProcessTypeRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final BpmTxnProcessInstanceRepository processInstanceRepo;
    private final BpmTxnTaskCommentRepository taskCommentRepo;
    private final ComCfgProcessTypeRepository processTypeRepo;
    private final ComInfOrganizationRepository orgRepo;
    private final ComCfgTxnContentRepository txnContentRepo;

    private final BpmService bpmService;

    private final CamundaExtensionPropertiesUtil propertiesUtil;

    @Data
    @Builder
    private static class ProcessStartContext {
        private ProcessData processData;
        private TaskBpmData bpmData;
        private Timestamp now;
        private String processDefinitionKey;
        private BpmTxnProcessInstance processInstance;
    }

    @Data
    @Builder
    private static class ProcessEndContext {
        private ProcessData processData;
        private TaskBpmData bpmData;
        private String processInstanceCode;
        private String processTypeCode;
        private String endStatus;
        private String currentActivityId;
        private Timestamp now;
    }

    @Override
    @Transactional
    public void afterStart(String processDefinitionKey, DelegateExecution execution) {
        log.info("ProcessService: Starting process instance processing for ID: {}", execution.getProcessInstanceId());

        // 1. get data
        ProcessStartContext ctx = getStartData(processDefinitionKey, execution);

        // 2. insert PROCESS_INSTANCE
        insertProcessInstance(ctx);

        // 3. insert TXN_AUDIT
        insertTxnAudit(ctx);

        // 5. insert DOC_FILE (TaskID = 'START')
        insertDocFileStart(ctx);

        // 6. insert TASK_COMMENT (TaskID = 'START')
        insertTaskCommentStart(ctx);
    }

    @Override
    @Transactional
    public void beforeEnd(String endStatus, DelegateExecution execution) {
        log.info("ProcessService: Ending process instance with status: {}", endStatus);

        // 1. get data
        ProcessEndContext ctx = getEndData(endStatus, execution);
        if (ctx.getProcessInstanceCode() == null)
            return;

        // 2. update PROCESS_INSTANCE
        updateProcessInstanceEnd(ctx);

        // 3. update DOC_FILE
        updateDocFileEnd(ctx);
    }

    // ========================================================================
    // START STEPS
    // ========================================================================

    private ProcessStartContext getStartData(String processDefinitionKey, DelegateExecution execution) {
        Map<String, String> properties = propertiesUtil.getProperties(execution);
        execution.setVariable(VariableConstants.IS_FULLSCREEN, properties.get(VariableConstants.IS_FULLSCREEN));

        ProcessData processData = VariablesUtil.getData(
                execution,
                VariableConstants.PROCESS_DATA,
                ProcessData.class);

        TaskBpmData bpmData = VariablesUtil.getData(
                execution,
                VariableConstants.TASK_BPM_DATA,
                TaskBpmData.class);

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        String defKey = processDefinitionKey.split(":")[0];

        return ProcessStartContext.builder()
                .processData(processData)
                .bpmData(bpmData)
                .now(now)
                .processDefinitionKey(defKey)
                .build();
    }

    private void insertProcessInstance(ProcessStartContext ctx) {
        ProcessData data = ctx.getProcessData();
        String processTypeCode = ctx.getProcessDefinitionKey(); // processTypeCode = processDefinitionKey
        Timestamp now = ctx.getNow();

        String processTypeName = processTypeRepo.getNameByCode(processTypeCode);
        String parentOrgCode = orgRepo.getParentCodeByOrgCode(data.getOrgCode());
        String finalTxnContent = generateTxnContent(data);

        BpmTxnProcessInstance processInstance = new BpmTxnProcessInstance();

        // Audit info

        // Basic info
        processInstance.setCreatedDate(now);
        processInstance.setBusinessStatus(StatusConstants.STATUS_ACTIVE);
        processInstance.setIsDelete(StatusConstants.ZERO);
        processInstance.setIsSuspend(StatusConstants.ZERO);
        processInstance.setTxnDate(now);

        // References
        processInstance.setProcessInstanceCode(data.getProcessInstanceCode());
        processInstance.setProcessTypeCode(processTypeCode);
        processInstance.setProcessTypeName(processTypeName);
        processInstance.setOrgCode(data.getOrgCode());
        processInstance.setParentCode(parentOrgCode);

        // Content & Customer
        processInstance.setTxnContent(finalTxnContent != null ? finalTxnContent : "");
        processInstance.setCustomerCode(data.getCustomerCode());
        processInstance.setCustomerName(data.getCustomerName());

        // SLA Calculation
        bpmService.applySlaSettings(processInstance, data.getOrgCode());

        processInstanceRepo.save(processInstance);
        ctx.setProcessInstance(processInstance); // Save for later steps
        log.info("Saved BpmTxnProcessInstance with Code: {}", processInstance.getProcessInstanceCode());
    }

    private void insertTxnAudit(ProcessStartContext ctx) {
        bpmService.buildAudits(ctx.getProcessData(), ctx.getBpmData(), ctx.getNow());
    }

    private void insertDocFileStart(ProcessStartContext ctx) {
        bpmService.persistFilesForStart(ctx.getProcessInstance(), ctx.getBpmData(), ctx.getProcessData(), ctx.getNow());
    }

    private void insertTaskCommentStart(ProcessStartContext ctx) {
        // We can get TaskBpmData again or use context. Context has it.
        // But original code got it slightly differently? No, same source.
        TaskBpmData taskBpmData = ctx.getBpmData();
        String taskComment = taskBpmData != null ? taskBpmData.getTaskComment() : null;
        BpmTxnProcessInstance pi = ctx.getProcessInstance();
        Timestamp now = ctx.getNow();

        BpmTxnTaskComment cmt = new BpmTxnTaskComment();
        cmt.setTxnDate(now);
        cmt.setProcessInstanceCode(pi.getProcessInstanceCode());
        cmt.setProcessTypeCode(pi.getProcessTypeCode());
        cmt.setTaskId(StatusConstants.TASK_ID_START);
        cmt.setIsDelete(StatusConstants.ZERO);
        cmt.setBusinessStatus(StatusConstants.STATUS_COMPLETE);
        cmt.setTaskStatus(StatusConstants.STATUS_COMPLETE);
        cmt.setTaskComments(taskComment);
        taskCommentRepo.save(cmt);
    }

    // ========================================================================
    // END STEPS
    // ========================================================================

    private ProcessEndContext getEndData(String endStatus, DelegateExecution execution) {
        ProcessData processData = VariablesUtil.getData(
                execution,
                VariableConstants.PROCESS_DATA,
                ProcessData.class);

        TaskBpmData bpmData = VariablesUtil.getData(
                execution,
                VariableConstants.TASK_BPM_DATA,
                TaskBpmData.class);

        String piCode = processData != null ? processData.getProcessInstanceCode() : null;
        String definitionId = execution.getProcessDefinitionId();
        // processTypeCode will be processDefinitionKey (assuming ID format like
        // 'key:version:id')
        String processTypeCode = definitionId != null ? definitionId.split(":")[0] : null;

        return ProcessEndContext.builder()
                .processData(processData)
                .bpmData(bpmData)
                .processInstanceCode(piCode)
                .processTypeCode(processTypeCode)
                .endStatus(endStatus)
                .currentActivityId(execution.getCurrentActivityId())
                .now(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    private void updateProcessInstanceEnd(ProcessEndContext ctx) {
        String piCode = ctx.getProcessInstanceCode();
        String endStatus = ctx.getEndStatus();

        processInstanceRepo.findByProcessInstanceCode(piCode).ifPresent(pi -> {
            boolean updated = false;
            if (StatusConstants.STATUS_APPROVE.equals(endStatus)) {
                pi.setBusinessStatus(StatusConstants.STATUS_COMPLETE);
                pi.setModifiedBy(ctx.getBpmData().getCurrentUser());
                updated = true;
            } else if (StatusConstants.CANCEL.equals(endStatus)) {
                pi.setBusinessStatus(StatusConstants.CANCEL);
                pi.setModifiedBy(ctx.getBpmData().getCurrentUser());
                updated = true;
            }
            if (updated) {
                if (pi.getSlaProcessDeadline() != null) {
                    if (pi.getSlaProcessDeadline().after(Timestamp.valueOf(LocalDateTime.now()))) {
                        pi.setSlaResult(StatusConstants.ACHIEVED);
                    } else {
                        pi.setSlaResult(StatusConstants.BREACHED);
                    }
                    pi.setSlaEvaluatedAt(Timestamp.valueOf(LocalDateTime.now()));
                }
                processInstanceRepo.save(pi);
            }
        });
    }

    private void updateDocFileEnd(ProcessEndContext ctx) {
        String piCode = ctx.getProcessInstanceCode();
        String endStatus = ctx.getEndStatus();
        String currentUser = ctx.getBpmData().getCurrentUser();

        if (StatusConstants.STATUS_APPROVE.equals(endStatus)) {
            bpmService.finalizeDocFiles(piCode, currentUser, StatusConstants.STATUS_COMPLETE);
        } else if (StatusConstants.CANCEL.equals(endStatus)) {
            bpmService.finalizeDocFiles(piCode, currentUser, StatusConstants.CANCEL);
        }
    }

    // ========================================================================
    // UTILS
    // ========================================================================

    private String generateTxnContent(ProcessData data) {
        if (data.getInterpretiveStructureDto() != null && data.getInterpretiveStructureDto().getContentCode() != null) {
            return txnContentRepo.findByContentCode(data.getInterpretiveStructureDto().getContentCode())
                    .map(contentConfig -> replacePlaceholders(contentConfig.getContentText(),
                            data.getInterpretiveStructureDto().getParamInterpretiveStructure()))
                    .orElse(data.getTxnContent());
        }
        return data.getTxnContent();
    }

    private String replacePlaceholders(String template, Map<String, String> params) {
        if (template == null || params == null || params.isEmpty()) {
            return template;
        }

        String result = template;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();

            if (isValidParam(key, val)) {
                Pattern p = Pattern.compile("@" + Pattern.quote(key) + "(?![A-Za-z0-9_])");
                Matcher m = p.matcher(result);
                result = m.replaceAll(Matcher.quoteReplacement(val));
            }
        }
        return result;
    }

    private boolean isValidParam(String key, String val) {
        return key != null && !key.isBlank() && val != null;
    }
}
