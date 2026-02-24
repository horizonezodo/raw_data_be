package ngvgroup.com.bpm.core.base.service.impl;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.base.dto.ProcessData;
import ngvgroup.com.bpm.core.base.dto.TaskBpmData;
import ngvgroup.com.bpm.core.base.dto.TaskCreationContext;
import ngvgroup.com.bpm.core.base.model.BpmTxnProcessInstance;
import ngvgroup.com.bpm.core.base.model.BpmTxnTaskComment;
import ngvgroup.com.bpm.core.base.model.BpmTxnTaskInbox;
import ngvgroup.com.bpm.core.base.repository.BpmTxnDocFileRepository;
import ngvgroup.com.bpm.core.base.repository.BpmTxnProcessInstanceRepository;
import ngvgroup.com.bpm.core.base.repository.BpmTxnTaskCommentRepository;
import ngvgroup.com.bpm.core.base.repository.BpmTxnTaskInboxRepository;
import ngvgroup.com.bpm.core.base.service.*;
import ngvgroup.com.bpm.core.contants.StatusConstants;
import ngvgroup.com.bpm.core.contants.VariableConstants;
import ngvgroup.com.bpm.core.utils.CamundaExtensionPropertiesUtil;
import ngvgroup.com.bpm.core.utils.VariablesUtil;
import ngvgroup.com.bpm.features.common.repository.ComInfOrganizationRepository;
import ngvgroup.com.bpm.features.rule.repository.ComCfgRuleRepository;
import ngvgroup.com.bpm.features.rule.repository.ComCfgRuleUserRepository;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.IdentityLinkType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final BpmTxnTaskInboxRepository taskInboxRepo;
    private final BpmTxnTaskCommentRepository taskCommentRepo;
    private final BpmTxnProcessInstanceRepository processInstanceRepo;
    private final BpmTxnDocFileRepository docFileRepo;
    private final ComInfOrganizationRepository orgRepo;

    // Repos for Assignment Logic
    private final ComCfgRuleUserRepository ruleUserRepository;

    // Core Service
    private final BpmService bpmService;

    private final CamundaExtensionPropertiesUtil propertiesUtil;
    private final ComCfgRuleRepository comCfgRuleRepository;

    @Data
    @Builder
    private static class TaskCreateContext {
        private ProcessData processData;
        private BpmTxnProcessInstance processInstance;
        private TaskCreationContext creationContext;
    }

    @Data
    @Builder
    private static class TaskClaimContext {
        private BpmTxnTaskInbox taskInbox;
        private String assignee;
        private Timestamp now;
    }

    @Data
    @Builder
    private static class TaskCompleteContext {
        private TaskBpmData taskBpmData;
        private ProcessData processData;
        private String processInstanceCode;
        private String processTypeCode;
        private String currentUser;
        private Timestamp now;
    }

    @Override
    @Transactional
    public void onTaskCreate(DelegateTask task) {
        // Idempotency check
        if (taskInboxRepo.existsByTaskId(task.getId()))
            return;

        // 1. get data
        TaskCreateContext ctx = getTaskCreateData(task);

        // 2. insert TASK_INBOX
        insertTaskInboxCreate(task, ctx);
    }

    @Override
    @Transactional
    public void afterClaim(String taskId, String assignee) {
        // 1. get data
        TaskClaimContext ctx = getTaskClaimData(taskId, assignee);
        if (ctx == null)
            return;

        // 2. update TASK_INBOX
        updateTaskInboxClaim(ctx);
    }

    @Override
    @Transactional
    public void afterComplete(String taskId, DelegateTask delegateTask, String processTypeCode) {
        // 1. get data
        TaskCompleteContext ctx = getTaskCompleteData(taskId, delegateTask, processTypeCode);
        if (ctx == null)
            return;

        // 2. update TASK_INBOX
        updateTaskInboxComplete(taskId, ctx);

        // 3. insert TASK_COMMENT
        insertTaskCommentComplete(taskId, ctx);

        // 4. insert DOC_FILE
        insertDocFileComplete(taskId, ctx);

        // 5. insert TXN_AUDIT
        insertTxnAuditComplete(ctx);
    }

    // ========================================================================
    // CREATE STEPS
    // ========================================================================

    private TaskCreateContext getTaskCreateData(DelegateTask task) {
        ProcessData processData = VariablesUtil.getData(task, VariableConstants.PROCESS_DATA, ProcessData.class);
        TaskBpmData taskBpmData = VariablesUtil.getData(task, VariableConstants.TASK_BPM_DATA, TaskBpmData.class);
        validateCreationData(processData);

        BpmTxnProcessInstance processInstance = processInstanceRepo
                .findByProcessInstanceCode(processData.getProcessInstanceCode()).orElse(null);

        TaskCreationContext creationCtx = buildCreationContext(task, processData, taskBpmData, processInstance);

        return TaskCreateContext.builder()
                .processData(processData)
                .processInstance(processInstance)
                .creationContext(creationCtx)
                .build();
    }

    private void insertTaskInboxCreate(DelegateTask task, TaskCreateContext ctx) {
        TaskCreationContext cCtx = ctx.getCreationContext();
        persistTaskInbox(task, cCtx);
    }

    // ========================================================================
    // CLAIM STEPS
    // ========================================================================

    private TaskClaimContext getTaskClaimData(String taskId, String assignee) {
        Optional<BpmTxnTaskInbox> taskInboxOpt = taskInboxRepo.findByTaskId(taskId);
        return taskInboxOpt.map(bpmTxnTaskInbox -> TaskClaimContext.builder()
                .taskInbox(bpmTxnTaskInbox)
                .assignee(assignee)
                .now(new Timestamp(System.currentTimeMillis()))
                .build()).orElse(null);

    }

    private void updateTaskInboxClaim(TaskClaimContext ctx) {
        BpmTxnTaskInbox taskInbox = ctx.getTaskInbox();
        Timestamp now = ctx.getNow();
        String assignee = ctx.getAssignee();

        taskInbox.setAcceptedBy(assignee);
        taskInbox.setAcceptedDate(now);
        taskInbox.setBusinessStatus(StatusConstants.STATUS_ACTIVE);

        if (taskInbox.getSlaTaskDeadline() == null && taskInbox.getSlaMaxDuration() != null) {
            long maxMinutes = taskInbox.getSlaMaxDuration();
            long millisToAdd = maxMinutes * 60 * 1000;
            taskInbox.setSlaTaskDeadline(new Timestamp(now.getTime() + millisToAdd));
        }

        taskInboxRepo.save(taskInbox);
    }

    // ========================================================================
    // COMPLETE STEPS
    // ========================================================================

    private TaskCompleteContext getTaskCompleteData(String taskId, DelegateTask delegateTask,
                                                    String processTypeCode) {
        TaskBpmData data = VariablesUtil.getData(delegateTask, VariableConstants.TASK_BPM_DATA, TaskBpmData.class);
        ProcessData processData = VariablesUtil.getData(delegateTask, VariableConstants.PROCESS_DATA, ProcessData.class);

        if (data == null) {
            log.warn("TaskBpmData is null for taskId: {}", taskId);
            return null;
        }

        String processInstanceCode = processData != null ? processData.getProcessInstanceCode() : null;
        String currentUser = data.getCurrentUser();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        return TaskCompleteContext.builder()
                .taskBpmData(data)
                .processData(processData)
                .processInstanceCode(processInstanceCode)
                .processTypeCode(processTypeCode)
                .currentUser(currentUser)
                .now(now)
                .build();
    }

    private void updateTaskInboxComplete(String taskId, TaskCompleteContext ctx) {
        Timestamp now = ctx.getNow();

        taskInboxRepo.findByTaskId(taskId).ifPresent(taskInbox -> {
            taskInbox.setBusinessStatus(StatusConstants.STATUS_COMPLETE);
            taskInbox.setTaskUpdateTime(now);

            if (taskInbox.getSlaTaskDeadline() != null) {
                boolean isAchieved = now.before(taskInbox.getSlaTaskDeadline());
                taskInbox.setSlaResult(isAchieved ? StatusConstants.ACHIEVED : StatusConstants.BREACHED);
                taskInbox.setSlaEvaluatedAt(now);
            }

            taskInboxRepo.save(taskInbox);
        });
    }

    private void insertTaskCommentComplete(String taskId, TaskCompleteContext ctx) {
        BpmTxnTaskComment taskComment = new BpmTxnTaskComment();
        Timestamp now = ctx.getNow();
        TaskBpmData data = ctx.getTaskBpmData();
        if(Objects.equals(data.getTaskStatus(), StatusConstants.SEND_APPROVE)) {
            data.setTaskStatus(StatusConstants.STATUS_COMPLETE);
        }

        taskComment.setTxnDate(now);
        taskComment.setProcessInstanceCode(ctx.getProcessInstanceCode());
        taskComment.setTaskId(taskId);
        taskComment.setProcessTypeCode(ctx.getProcessTypeCode());

        taskComment.setBusinessStatus(StatusConstants.STATUS_COMPLETE);
        taskComment.setTaskStatus(data.getTaskStatus());
        taskComment.setTaskComments(data.getTaskComment());

        taskCommentRepo.save(taskComment);
    }

    private void insertDocFileComplete(String taskId, TaskCompleteContext ctx) {
        bpmService.handleFilesForResubmit(taskId,
                ctx.getProcessInstanceCode(),
                ctx.getTaskBpmData(),
                ctx.getProcessData(),
                ctx.getCurrentUser(),
                ctx.getNow());
    }

    private void insertTxnAuditComplete(TaskCompleteContext ctx) {
        if (!CollectionUtils.isEmpty(ctx.getTaskBpmData().getTaskAudits())) {
            bpmService.persistAuditsForComplete(
                    ctx.getProcessData().getOrgCode(),
                    ctx.getProcessInstanceCode(),
                    ctx.getCurrentUser(),
                    ctx.getNow(),
                    ctx.getTaskBpmData());
        }
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================

    private void validateCreationData(ProcessData data) {
        if (data == null || data.getOrgCode() == null || data.getProcessInstanceCode() == null) {
            log.error("Missing required vars for task creation");
        }
    }

    private TaskCreationContext buildCreationContext(DelegateTask task, ProcessData data, TaskBpmData bpmData,
                                                     BpmTxnProcessInstance processInstance) {
        Timestamp createdTime = task.getCreateTime() != null
                ? new Timestamp(task.getCreateTime().getTime())
                : Timestamp.valueOf(LocalDateTime.now());

        Timestamp txnTime = (processInstance != null && processInstance.getTxnDate() != null)
                ? processInstance.getTxnDate()
                : createdTime;

        String orgName = orgRepo.getOrgNameByOrgCode(data.getOrgCode()).orElse(null);

        String createdBy = (processInstance != null && processInstance.getCreatedBy() != null)
                ? processInstance.getCreatedBy()
                : StatusConstants.DEFAULT_USER;

        String approvedBy = taskInboxRepo.findLatestApprovedBy(data.getProcessInstanceCode()).orElse(createdBy);

        String candidateGroup = task.getCandidates().stream()
                .filter(candidate -> IdentityLinkType.CANDIDATE.equals(candidate.getType())
                        && candidate.getGroupId() != null)
                .map(IdentityLink::getGroupId)
                .findFirst().orElse(null);

        return new TaskCreationContext(
                createdTime, txnTime,
                data.getOrgCode(), orgName,
                data.getProcessInstanceCode(),
                createdBy, approvedBy, bpmData.getCurrentUser(),
                candidateGroup, data.getReferenceCode(), processInstance);
    }

    private void persistTaskInbox(DelegateTask task, TaskCreationContext ctx) {
        BpmTxnTaskInbox inbox = new BpmTxnTaskInbox();
        BpmTxnProcessInstance processInstance = ctx.processInstance();
        String processTypeCode = processInstance != null ? processInstance.getProcessTypeCode() : null;

        inbox.setCreatedDate(ctx.createdTime());

        // Base Info
        inbox.setTaskId(task.getId());
        inbox.setTaskDefineCode(task.getTaskDefinitionKey());
        inbox.setTaskDefineName(task.getName());
        inbox.setProcessInstanceCode(ctx.processInstanceCode());
        inbox.setProcessTypeCode(processTypeCode);
        inbox.setOrgCode(ctx.orgCode());
        inbox.setOrgName(ctx.orgName());

        // Status & Dates
        inbox.setBusinessStatus("UNASSIGNED");
        inbox.setTxnDate(ctx.transactionTime());
        inbox.setTaskStartTime(ctx.createdTime());
        inbox.setTaskUpdateTime(ctx.createdTime());
        inbox.setIsDelete(StatusConstants.ZERO);
        inbox.setIsSuspend(StatusConstants.ZERO);

        if (processInstance != null) {
            inbox.setCustomerCode(processInstance.getCustomerCode());
            inbox.setCustomerName(processInstance.getCustomerName());
        }

        // Config Properties
        Map<String, String> props = propertiesUtil.getProperties(task);
        if (props != null) {
            inbox.setFormKey(props.get(VariableConstants.FORM_KEY));
            inbox.setFormAction(props.get(VariableConstants.FORM_ACTION));
            inbox.setPathComplete(props.get(VariableConstants.PATH_COMPLETE));
        }

        inbox.setPrevActionBy(ctx.prevActionBy());

        // Apply Logic
        bpmService.applySla(inbox, ctx.orgCode(), task.getTaskDefinitionKey(), processTypeCode);

        assignCandidateUsers(task, ctx, inbox);
        taskInboxRepo.save(inbox);
    }

    // ========================================================================
    // ASSIGNMENT LOGIC (Formerly BpmAssignmentService)
    // ========================================================================

    private void assignCandidateUsers(DelegateTask task, TaskCreationContext ctx, BpmTxnTaskInbox inbox) {
        Set<String> groups = task.getCandidates().stream()
                .filter(candidate -> IdentityLinkType.CANDIDATE.equals(candidate.getType())
                        && candidate.getGroupId() != null)
                .map(IdentityLink::getGroupId)
                .collect(Collectors.toSet());

        if (groups.isEmpty())
            return;
        String parentCode = groups.iterator().next();
        String ruleCode = comCfgRuleRepository.findRuleCode(ctx.orgCode(), parentCode).orElse(null);
        inbox.setRuleCode(ruleCode);
        List<String> userIds = ruleUserRepository.findUserCodeByRuleCode(ruleCode);
        inbox.setAssignTo(String.join(StatusConstants.COMMA, userIds));
    }
}
