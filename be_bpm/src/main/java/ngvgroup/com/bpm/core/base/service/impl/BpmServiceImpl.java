package ngvgroup.com.bpm.core.base.service.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.base.dto.*;
import ngvgroup.com.bpm.core.base.model.AudTxnAudit;
import ngvgroup.com.bpm.core.base.model.BpmTxnDocFile;
import ngvgroup.com.bpm.core.base.model.BpmTxnProcessInstance;
import ngvgroup.com.bpm.core.base.model.BpmTxnTaskInbox;
import ngvgroup.com.bpm.core.base.repository.*;
import ngvgroup.com.bpm.core.base.service.BpmService;
import ngvgroup.com.bpm.core.contants.BpmErrorCode;
import ngvgroup.com.bpm.core.contants.StatusConstants;
import ngvgroup.com.bpm.core.contants.VariableConstants;
import ngvgroup.com.bpm.features.com_cfg_process_file.repository.ComCfgProcessFileRepository;
import ngvgroup.com.bpm.features.common.repository.ComCfgParameterRepository;
import ngvgroup.com.bpm.features.ctg_cfg_resource.repository.CtgCfgResourceMappingRepository;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDtlDto;
import ngvgroup.com.bpm.features.sla.dto.ComCfgSlaProcessDto;
import ngvgroup.com.bpm.features.sla.model.ComCfgSlaTaskDtl;
import ngvgroup.com.bpm.features.sla.repository.ComCfgSlaProcessRepository;
import ngvgroup.com.bpm.features.sla.repository.ComCfgSlaTaskDtlRepository;
import ngvgroup.com.bpm.features.transaction.dto.CustomerTransactionHistoryDto;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static org.camunda.spin.Spin.JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BpmServiceImpl extends BaseStoredProcedureService implements BpmService {

    private final ComCfgProcessFileRepository processFileRepository;
    private final BpmTxnTaskCommentRepository taskCommentRepository;
    private final BpmTxnTaskInboxRepository taskInboxRepository;
    private final BpmTxnDocFileRepository docFileRepository;
    private final AudTxnAuditRepository auditRepository;
    private final ProcessEngine processEngine;
    private final BpmTxnProcessInstanceRepository processInstanceRepo;
    private final ObjectMapper objectMapper;

    // SLA Repos
    private final ComCfgSlaProcessRepository slaProcessRepo;
    private final ComCfgSlaTaskDtlRepository slaTaskRepo;
    private final ComCfgParameterRepository paramRepo;

    // HRM repo
    private final HrmInfEmployeeRepository hrmInfoRepository;

    private final CtgCfgResourceMappingRepository resourceMappingRepository;

    /**
     * Context object for DocFile processing to reduce parameter count
     */
    @Builder
    @Getter
    private static class DocFileContext {
        private BpmTxnProcessInstance processInstance;
        private String referenceCode;
        private Timestamp now;
        private Date txnDate;
        private String taskId;
        private String createdBy;
    }

    @Override
    public TaskViewBpmData getDetail(String taskId) {
        BpmTxnTaskInbox taskInbox = taskInboxRepository.findByTaskId(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Task id %s", taskId));
        String processInstanceCode = taskInbox.getProcessInstanceCode();
        String processTypeCode = taskInbox.getProcessTypeCode();

        return getDetailFromInstanceCode(taskId, processInstanceCode, processTypeCode);
    }

    private Object parseJsonIfPossible(Object value) {
        if (value instanceof String str &&
                ((str.startsWith("{") && str.endsWith("}")) || (str.startsWith("[") && str.endsWith("]")))) {
            try {
                return objectMapper.readValue(str, Object.class);
            } catch (Exception e) {
                // ignore, return original string
            }
        }
        return value;
    }

    private String toJsonString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String string) {
            return string;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            log.error("Cannot convert to json", e);
            return value.toString();
        }
    }

    /**
     * Convert complex objects (Map, List) to SpinJsonNode for proper JSON storage
     * in Camunda.
     * This mimics how Camunda REST API handles JSON variables.
     * Primitive types (String, Number, Boolean) are left unchanged.
     */
    private Map<String, Object> convertToSpinVariables(Map<String, Object> variables) {
        if (variables == null) {
            return new HashMap<>();
        }

        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            Object value = entry.getValue();
            if (value == null) {
                result.put(entry.getKey(), null);
            } else if (value instanceof Map || value instanceof List) {
                // Convert complex objects to SpinJsonNode
                try {
                    String jsonString = objectMapper.writeValueAsString(value);
                    result.put(entry.getKey(), JSON(jsonString));
                } catch (Exception e) {
                    log.warn("Failed to convert variable {} to JSON, using raw value", entry.getKey(), e);
                    result.put(entry.getKey(), value);
                }
            } else {
                // Keep primitives as-is (String, Number, Boolean, etc.)
                result.put(entry.getKey(), value);
            }
        }
        return result;
    }

    private TaskViewBpmData getDetailFromInstanceCode(String taskId, String processInstanceCode,
            String processTypeCode) {
        BpmTxnProcessInstance processInstance = processInstanceRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "ProcessInstance"));

        // 1. Data Retrieval
        List<ProcessFileDto> files = getProcessFiles(taskId, processInstanceCode, processTypeCode);
        List<CommentDto> comments = getComments(processInstance);
        List<TrackingDto> trackings = getTrackings(processInstance);
        List<AuditDto> audits = getAudits(processInstanceCode);

        // 2. Batch Enrichment
        enrichUserData(comments, trackings);

        // 3. Status Retrieval
        String businessStatus = taskInboxRepository.findTopByProcessInstanceCodeOrderByIdDesc(processInstanceCode)
                .map(BpmTxnTaskInbox::getBusinessStatus)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.NOT_FOUND, "ProcessInstanceCode", processInstanceCode));

        return TaskViewBpmData.builder()
                .processInstanceCode(processInstanceCode)
                .businessStatus(businessStatus)
                .taskProcessFiles(files)
                .taskComments(comments)
                .taskTrackings(trackings)
                .taskAudits(audits)
                .build();
    }

    private List<ProcessFileDto> getProcessFiles(String taskId, String instanceCode, String typeCode) {
        String fileTaskId = docFileRepository.findTopByProcessInstanceCodeOrderByIdDesc(instanceCode)
                .map(BpmTxnDocFile::getTaskId)
                .orElse(taskId);

        List<FileFlatDto> flatList = processFileRepository.findFilesByTaskId(fileTaskId, instanceCode,
                typeCode, VariableConstants.TAB_GROUP_CODE_LIKE);
        return processFiles(flatList);
    }

    private List<AuditDto> getAudits(String instanceCode) {
        List<AuditDto> audits = auditRepository.findAuditByInstance(instanceCode);
        audits.forEach(audit -> {
            audit.setOldValue(parseJsonIfPossible(audit.getOldValue()));
            audit.setNewValue(parseJsonIfPossible(audit.getNewValue()));
        });
        return audits;
    }

    private List<CommentDto> getComments(BpmTxnProcessInstance instance) {
        List<CommentDto> comments = taskCommentRepository.findCommentsByInstance(instance.getProcessInstanceCode());
        String defaultTaskName = String.format("Khởi tạo %s", instance.getProcessTypeName());

        comments.stream()
                .filter(c -> c.getTaskName() == null || c.getTaskName().isEmpty())
                .forEach(c -> c.setTaskName(defaultTaskName));
        return comments;
    }

    private List<TrackingDto> getTrackings(BpmTxnProcessInstance instance) {
        List<TrackingDto> trackings = new ArrayList<>(
                taskInboxRepository.findInboxByInstance(instance.getProcessInstanceCode()));
        trackings.add(TrackingDto.builder()
                .status("COMPLETE")
                .taskStartTime(instance.getCreatedDate())
                .acceptedDate(instance.getCreatedDate())
                .completeTime(instance.getCreatedDate())
                .executorCode(instance.getCreatedBy())
                .taskName(String.format("Khởi tạo %s", instance.getProcessTypeName()))
                .build());
        return trackings;
    }

    private void enrichUserData(List<CommentDto> comments, List<TrackingDto> trackings) {
        Set<String> userNames = Stream.concat(
                comments.stream().map(CommentDto::getCreatedByCode),
                trackings.stream().map(TrackingDto::getExecutorCode)).filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (userNames.isEmpty())
            return;

        Map<String, HrmInfoDto> userMap = hrmInfoRepository.findByUsername(new ArrayList<>(userNames))
                .stream().collect(Collectors.toMap(HrmInfoDto::getUsername, f -> f, (a, b) -> a));

        comments.forEach(c -> Optional.ofNullable(userMap.get(c.getCreatedByCode())).ifPresent(info -> {
            c.setCreatedByName(info.getEmployeeName());
            c.setCreatedByPosition(info.getPositionName());
        }));

        trackings.forEach(t -> Optional.ofNullable(userMap.get(t.getExecutorCode()))
                .ifPresent(info -> t.setExecutorName(info.getEmployeeName())));
    }

    private List<ProcessFileDto> processFiles(List<FileFlatDto> flatList) {
        Map<String, ProcessFileDto> map = new LinkedHashMap<>();

        for (FileFlatDto flat : flatList) {
            map.putIfAbsent(flat.getProcessFileCode(), ProcessFileDto.builder()
                    .processFileCode(flat.getProcessFileCode())
                    .processFileName(flat.getProcessFileName())
                    .files(new ArrayList<>())
                    .build());

            if (flat.getFilePath() != null) {
                FileDto detail = FileDto.builder()
                        .fileId(flat.getFileId())
                        .fileName(flat.getFileName())
                        .filePath(flat.getFilePath())
                        .fileSize(flat.getFileSize())
                        .build();

                map.get(flat.getProcessFileCode()).getFiles().add(detail);
            }
        }
        return new ArrayList<>(map.values());
    }

    @Override
    @Transactional
    public void claimTask(String taskId) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (task == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "Task id %s", taskId);
        }

        String userId = getCurrentUserName();
        try {
            if (!taskInboxRepository.claimable(taskId, userId)) {
                throw new BusinessException(BpmErrorCode.USER_CANNOT_CLAIM_TASK);
            }
            taskService.claim(taskId, userId);
        } catch (ProcessEngineException e) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Tác vụ đã được thực hiện bởi " + task.getAssignee());
        }
    }

    @Override
    public List<ProcessFileDto> getProcessFilesFromReferenceCode(String referenceCode, String processTypeCode) {
        List<TaskRefDto> taskRefs = docFileRepository.findLatestTaskInfoByReference(referenceCode);

        if (taskRefs.isEmpty()) {
            return new ArrayList<>();
        }

        // Take the first result as it represents the latest snapshot
        TaskRefDto latestTask = taskRefs.get(0);

        List<FileFlatDto> flatList = processFileRepository.findFilesByTaskId(
                latestTask.getTaskId(),
                latestTask.getProcessInstanceCode(),
                processTypeCode,
                VariableConstants.TAB_GROUP_CODE_LIKE);
        return processFiles(flatList);
    }

    @Override
    @Transactional
    public void saveDraft(DraftTaskBpmData request) {
        BpmTxnTaskInbox taskInbox = taskInboxRepository.findByTaskId(request.getTaskId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Task id %s", request.getTaskId()));

        processDraftFiles(request, taskInbox);
        processDraftAudits(request, taskInbox);
    }

    private void processDraftFiles(DraftTaskBpmData request, BpmTxnTaskInbox taskInbox) {
        String taskId = request.getTaskId();
        String processTypeCode = taskInbox.getProcessTypeCode();

        docFileRepository.deleteAll(docFileRepository.findByTaskId(taskId));

        if (request.getTaskProcessFiles() == null || request.getTaskProcessFiles().isEmpty()) {
            return;
        }

        List<ComCfgProcessFileDto> fileConfigs = processFileRepository.findByProcessTypeCode(processTypeCode);

        if (fileConfigs == null || fileConfigs.isEmpty()) {
            return;
        }

        Map<String, ComCfgProcessFileDto> configMap = fileConfigs.stream()
                .collect(Collectors.toMap(ComCfgProcessFileDto::getProcessFileCode, Function.identity(), (a, b) -> a));

        List<BpmTxnDocFile> newDocFiles = new ArrayList<>();

        for (ProcessFileDto group : request.getTaskProcessFiles()) {
            ComCfgProcessFileDto config = configMap.get(group.getProcessFileCode());
            if (config == null) {
                continue;
            }
            if (group.getFiles() != null) {
                for (FileDto fileDto : group.getFiles()) {
                    newDocFiles.add(mapToDocFileForDraft(fileDto, group.getProcessFileCode(), config, taskInbox));
                }
            }
        }

        if (!newDocFiles.isEmpty()) {
            docFileRepository.saveAll(newDocFiles);
        }
    }

    private BpmTxnDocFile mapToDocFileForDraft(FileDto fileDto, String processFileCode, ComCfgProcessFileDto config,
            BpmTxnTaskInbox taskInbox) {
        BpmTxnDocFile docFile = new BpmTxnDocFile();
        docFile.setBusinessStatus(StatusConstants.STATUS_ACTIVE);
        docFile.setTxnDate(taskInbox.getTxnDate());
        docFile.setProcessTypeCode(taskInbox.getProcessTypeCode());
        docFile.setProcessInstanceCode(taskInbox.getProcessInstanceCode());
        docFile.setTaskId(taskInbox.getTaskId());
        docFile.setReferenceCode(taskInbox.getProcessInstanceCode());
        docFile.setCustomerCode(taskInbox.getCustomerCode());
        docFile.setProcessFileCode(processFileCode);
        docFileMapHelper(fileDto, config, docFile);
        docFile.setFileId(fileDto.getFileId());
        return docFile;
    }

    private void processDraftAudits(DraftTaskBpmData request, BpmTxnTaskInbox taskInbox) {
        String instanceCode = taskInbox.getProcessInstanceCode();
        String orgCode = taskInbox.getOrgCode();

        if (request.getTaskAudits() == null || request.getTaskAudits().isEmpty()) {
            return;
        }

        List<AudTxnAudit> newAudits = request.getTaskAudits().stream()
                .map(dto -> mapToAuditDraft(dto, instanceCode, orgCode))
                .toList();

        if (!newAudits.isEmpty()) {
            auditRepository.saveAll(newAudits);
        }
    }

    private AudTxnAudit mapToAuditDraft(AuditDto dto, String instanceCode, String orgCode) {
        AudTxnAudit audit = new AudTxnAudit();
        audit.setTxnDate(new java.util.Date());
        audit.setProcessInstanceCode(instanceCode);
        audit.setOrgCode(orgCode);
        audit.setFieldName(dto.getFieldName());
        audit.setFieldCode(dto.getFieldCode());
        audit.setOldValue(toJsonString(dto.getOldValue()));
        audit.setNewValue(toJsonString(dto.getNewValue()));
        return audit;
    }

    @Override
    public FileDto getFileDetail(String fileId) {
        return docFileRepository.findByFileId(fileId);
    }

    // ========================================================================
    // SLA IMPLEMENTATION
    // ========================================================================

    @Override
    public void applySlaSettings(BpmTxnProcessInstance pi, String orgCode) {
        var slaProcessOpt = slaProcessRepo.findSlaProcess(orgCode, pi.getProcessTypeCode());
        if (slaProcessOpt.isEmpty())
            return;

        ComCfgSlaProcessDto slaProcess = slaProcessOpt.get();
        pi.setSlaProcessCode(slaProcess.getProcessDefineCode());
        pi.setSlaType(slaProcess.getSlaType());

        var slaDtlOpt = slaProcessRepo.findLatestSlaDtl(orgCode, slaProcess.getProcessDefineCode());
        if (slaDtlOpt.isEmpty())
            return;

        ComCfgSlaProcessDtlDto dtl = slaDtlOpt.get();
        String unit = slaProcess.getUnit();

        long maxMinutes = convertToMinutes(dtl.getSlaMaxDuration(), unit);
        long warningMinutes = calculateWarningMinutes(dtl, unit, maxMinutes);

        pi.setSlaMaxDuration(maxMinutes);
        pi.setSlaWarningType(dtl.getSlaWarningType());
        pi.setSlaWarningDuration(warningMinutes);
        pi.setSlaWarningPercent(dtl.getSlaWarningPercent() != null ? Math.round(dtl.getSlaWarningPercent()) : 0L);

        pi.setSlaProcessDeadline(calculateDeadline(pi.getCreatedDate(), maxMinutes));
    }

    @Override
    public void applySla(BpmTxnTaskInbox inbox, String orgCode, String taskDefKey, String procTypeCode) {
        ComCfgSlaTaskDtl dtl = slaTaskRepo
                .findByOrgCodeAndTaskDefineCodeAndProcessDefineCode(orgCode, taskDefKey, procTypeCode).orElse(null);
        if (dtl == null)
            return;

        String unit = slaProcessRepo.findSlaProcess(orgCode, procTypeCode).map(ComCfgSlaProcessDto::getUnit)
                .orElse(null);
        long maxMinutes = convertToMinutes(dtl.getSlaMaxDuration(), unit);

        inbox.setSlaMaxDuration(maxMinutes);
        inbox.setSlaWarningType(dtl.getSlaWarningType());
        inbox.setSlaWarningDuration(convertToMinutes(dtl.getSlaWarningDuration(), unit));
        inbox.setSlaWarningPercent(dtl.getSlaWarningPercent() != null ? Math.round(dtl.getSlaWarningPercent()) : 0L);

        // Check param: SLA_START_TIME_MODE
        var paramOpt = paramRepo.findByParamCodeAndIsActiveTrueAndIsDeleteFalse(VariableConstants.SLA_START_TIME_MODE);
        boolean isAcceptedMode = paramOpt.isPresent()
                && StatusConstants.SLA_MODE_ACCEPTED.equalsIgnoreCase(paramOpt.get().getParamValue());

        if (!isAcceptedMode) {
            inbox.setSlaTaskDeadline(new Timestamp(inbox.getCreatedDate().getTime() + (maxMinutes * 60 * 1000)));
        }
    }

    private long convertToMinutes(Double duration, String unit) {
        if (duration == null)
            return 0L;
        return Math.round(StatusConstants.SLA_UNIT_HOUR_CODE.equals(unit) ? duration * 60 : duration);
    }

    private long calculateWarningMinutes(ComCfgSlaProcessDtlDto dtl, String unit, long maxMinutes) {
        if (StatusConstants.SLA_UNIT_HOUR_CODE.equals(unit)) {
            return maxMinutes;
        }
        return convertToMinutes(dtl.getSlaWarningDuration(), unit);
    }

    private Timestamp calculateDeadline(java.util.Date baseDate, long minutesToAdd) {
        if (baseDate == null)
            return null;
        LocalDateTime ldt = new Timestamp(baseDate.getTime()).toLocalDateTime().plusMinutes(minutesToAdd);
        return Timestamp.valueOf(ldt);
    }

    // ========================================================================
    // AUDIT IMPLEMENTATION
    // ========================================================================

    @Override
    public void buildAudits(ProcessData processData, TaskBpmData bpmData, Timestamp now) {
        if (processData == null || bpmData == null || CollectionUtils.isEmpty(bpmData.getTaskAudits())) {
            return;
        }

        List<AudTxnAudit> auditEntities = new ArrayList<>();

        for (AuditDto dto : bpmData.getTaskAudits()) {
            if (dto.getFieldName() == null || dto.getFieldName().isBlank()) {
                continue;
            }

            AudTxnAudit audit = new AudTxnAudit();
            audit.setOrgCode(processData.getOrgCode());
            audit.setTxnDate(now);
            audit.setProcessInstanceCode(processData.getProcessInstanceCode());

            audit.setFieldName(dto.getFieldName());
            audit.setFieldCode(dto.getFieldCode());
            audit.setOldValue(toJsonString(dto.getOldValue()));
            audit.setNewValue(toJsonString(dto.getNewValue()));

            audit.setIsDelete(StatusConstants.ZERO);

            auditEntities.add(audit);
        }

        if (!auditEntities.isEmpty()) {
            auditRepository.saveAll(auditEntities);
        }
    }

    @Override
    public void persistAuditsForComplete(String orgCode, String piCode, String createdBy, Timestamp now,
            TaskBpmData taskBpmData) {
        if (taskBpmData == null || taskBpmData.getTaskAudits() == null || taskBpmData.getTaskAudits().isEmpty())
            return;

        List<AudTxnAudit> auditsToSave = new ArrayList<>();
        for (AuditDto auditDto : taskBpmData.getTaskAudits()) {
            if (!Objects.equals(auditDto.getOldValue(), auditDto.getNewValue())) {
                AudTxnAudit audit = new AudTxnAudit();
                audit.setIsDelete(StatusConstants.ZERO);
                audit.setOrgCode(orgCode);
                audit.setTxnDate(now);
                audit.setProcessInstanceCode(piCode);
                audit.setFieldName(auditDto.getFieldName());
                audit.setFieldCode(auditDto.getFieldCode());
                audit.setOldValue(toJsonString(auditDto.getOldValue()));
                audit.setNewValue(toJsonString(auditDto.getNewValue()));
                auditsToSave.add(audit);
            }
        }
        if (!auditsToSave.isEmpty()) {
            auditRepository.saveAll(auditsToSave);
        }
    }

    // ========================================================================
    // DOC FILE IMPLEMENTATION
    // ========================================================================

    @Override
    public void persistFilesForStart(BpmTxnProcessInstance processInstance, TaskBpmData taskBpmData,
            ProcessData processData, Timestamp now) {
        List<ComCfgProcessFileDto> configs = processFileRepository
                .findByProcessTypeCode(processInstance.getProcessTypeCode());

        if (configs == null || configs.isEmpty())
            return;

        List<ProcessFileDto> userFiles = (taskBpmData != null && taskBpmData.getTaskProcessFiles() != null)
                ? taskBpmData.getTaskProcessFiles()
                : Collections.emptyList();

        validateMandatoryFiles(configs, userFiles);

        if (userFiles.isEmpty())
            return;

        // Create Context
        DocFileContext context = DocFileContext.builder()
                .processInstance(processInstance)
                .referenceCode(processData.getReferenceCode())
                .now(now)
                .txnDate(new Date(processInstance.getTxnDate().getTime()))
                .taskId(StatusConstants.TASK_ID_START)
                .createdBy(processInstance.getCreatedBy())
                .build();

        List<BpmTxnDocFile> entities = buildDocFileList(configs, userFiles, context);

        if (!entities.isEmpty()) {
            docFileRepository.saveAll(entities);
        }
    }

    @Override
    public void handleFilesForResubmit(String taskId, String processInstanceCode, TaskBpmData taskBpmData,
            ProcessData processData, String currentUser, Timestamp now) {
        BpmTxnProcessInstance pi = processInstanceRepo.findByProcessInstanceCode(processInstanceCode).orElse(null);
        if (pi == null)
            return;

        List<ComCfgProcessFileDto> configs = processFileRepository.findByProcessTypeCode(pi.getProcessTypeCode());
        if (configs == null || configs.isEmpty())
            return;

        List<ProcessFileDto> userFiles = (taskBpmData != null && taskBpmData.getTaskProcessFiles() != null)
                ? taskBpmData.getTaskProcessFiles()
                : Collections.emptyList();

        if (StatusConstants.SEND_APPROVE.equals(taskBpmData.getTaskStatus())) {
            validateMandatoryFiles(configs, userFiles);
        }

        if (userFiles.isEmpty())
            return;

        // Create Context
        DocFileContext context = DocFileContext.builder()
                .processInstance(pi)
                .referenceCode(processData.getReferenceCode())
                .now(now)
                .txnDate(new Date(now.getTime()))
                .taskId(taskId)
                .createdBy(currentUser)
                .build();

        List<BpmTxnDocFile> entities = buildDocFileList(configs, userFiles, context);

        if (!entities.isEmpty()) {
            docFileRepository.saveAll(entities);
        }
    }

    @Override
    public void finalizeDocFiles(String piCode, String currentUser, String endStatus) {
        List<BpmTxnDocFile> files = docFileRepository.findByProcessInstanceCode(piCode);
        if (files == null || files.isEmpty())
            return;

        List<BpmTxnDocFile> toUpdate = new ArrayList<>();
        for (BpmTxnDocFile f : files) {
            if (StatusConstants.STATUS_ACTIVE.equals(f.getBusinessStatus())) {
                boolean changed = false;
                if (StatusConstants.STATUS_COMPLETE.equals(endStatus)) {
                    f.setBusinessStatus(StatusConstants.STATUS_COMPLETE);
                    changed = true;
                } else if (StatusConstants.CANCEL.equals(endStatus)) {
                    f.setBusinessStatus(StatusConstants.CANCEL);
                    changed = true;
                }
                if (changed)
                    toUpdate.add(f);
            }
        }
        if (!toUpdate.isEmpty()) {
            docFileRepository.saveAll(toUpdate);
        }
    }

    // =================================================================================
    // PRIVATE SHARED METHODS
    // =================================================================================

    /**
     * Logic kiểm tra file bắt buộc:
     * Duyệt qua cấu hình, nếu thấy fileTypeCode = '001' mà user không gửi lên ->
     * Báo lỗi.
     */
    private void validateMandatoryFiles(List<ComCfgProcessFileDto> configs, List<ProcessFileDto> userFiles) {
        for (ComCfgProcessFileDto cfg : configs) {
            if ("001".equals(cfg.getFileTypeCode())) {
                boolean isUploaded = userFiles.stream()
                        .anyMatch(uf -> cfg.getProcessFileCode().equals(uf.getProcessFileCode())
                                && uf.getFiles() != null
                                && !uf.getFiles().isEmpty());

                if (!isUploaded) {
                    throw new BusinessException(BpmErrorCode.PROCESS_FILE_MISSING, cfg.getProcessFileName());
                }
            }
        }
    }

    /**
     * Helper to build list of DocFiles using Context
     */
    private List<BpmTxnDocFile> buildDocFileList(List<ComCfgProcessFileDto> configs,
            List<ProcessFileDto> userFiles,
            DocFileContext context) {

        List<BpmTxnDocFile> entities = new ArrayList<>();

        Map<String, ComCfgProcessFileDto> configMap = configs.stream()
                .collect(Collectors.toMap(ComCfgProcessFileDto::getProcessFileCode, Function.identity()));

        for (ProcessFileDto pfDto : userFiles) {
            ComCfgProcessFileDto cfg = configMap.get(pfDto.getProcessFileCode());

            if (cfg != null && pfDto.getFiles() != null) {
                for (FileDto f : pfDto.getFiles()) {
                    BpmTxnDocFile docFile = mapToDocFile(f, cfg, context);

                    docFile.setFileId(UUID.randomUUID().toString());
                    docFile.setBusinessStatus(StatusConstants.STATUS_COMPLETE);

                    entities.add(docFile);
                }
            }
        }
        return entities;
    }

    private BpmTxnDocFile mapToDocFile(FileDto f, ComCfgProcessFileDto cfg, DocFileContext context) {
        BpmTxnDocFile docFile = new BpmTxnDocFile();
        docFile.setIsDelete(StatusConstants.ZERO);
        docFile.setProcessTypeCode(context.getProcessInstance().getProcessTypeCode());
        docFile.setProcessInstanceCode(context.getProcessInstance().getProcessInstanceCode());
        docFile.setReferenceCode(context.getReferenceCode());
        docFile.setTaskId(context.getTaskId());
        docFile.setCustomerCode(context.getProcessInstance().getCustomerCode());
        docFile.setProcessFileCode(cfg.getProcessFileCode());

        docFileMapHelper(f, cfg, docFile);
        docFile.setTxnDate(context.getTxnDate());

        return docFile;
    }

    private void docFileMapHelper(FileDto fileDto, ComCfgProcessFileDto config, BpmTxnDocFile docFile) {
        docFile.setIsAvatar(Objects.requireNonNullElse(config.getIsAvatar(), 0));
        docFile.setIsSent(Objects.requireNonNullElse(config.getIsSent(), 0));
        docFile.setIsPrint(Objects.requireNonNullElse(config.getIsPrint(), 0));
        docFile.setFileName(fileDto.getFileName());
        docFile.setFileSize(fileDto.getFileSize());
        docFile.setFilePath(fileDto.getFilePath());
    }

    @Override
    public String getTaskDefinitionKeyFromCamunda(String taskId) {
        BpmTxnTaskInbox task = taskInboxRepository.findByTaskId(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        return task.getTaskDefineCode();
    }

    @Override
    @Transactional
    public ProcessInstanceDto startProcess(String processDefinitionKey,
            StartProcessRequestDto requestBody) {

        if (!resourceMappingRepository.existsByUserIdAndResourceTypeCodeAndResourceCodeAndIsDelete(
                requestBody.getUserId(), "CM032.002", processDefinitionKey, 0)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        String businessKey = requestBody.getBusinessKey();

        // Set authenticated user - giống như Camunda engine-rest API
        // Điều này quan trọng để các listeners biết ai đang thực hiện action
        try {
            processEngine.getIdentityService().setAuthenticatedUserId(requestBody.getUserId());

            // Convert complex objects (Map, List) to SpinJsonNode - giống Camunda REST API
            Map<String, Object> variables = convertToSpinVariables(requestBody.getVariables());

            ProcessInstance instance;
            if (businessKey != null && !businessKey.isEmpty()) {
                instance = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey,
                        businessKey,
                        variables);
            } else {
                instance = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey,
                        variables);
            }

            return ProcessInstanceDto.fromProcessInstance(instance);
        } finally {
            // Clear authenticated user sau khi hoàn tất
            processEngine.getIdentityService().clearAuthentication();
        }
    }

    @Override
    public List<CustomerTransactionHistoryDto> getCustomerTransactionHistory(String customerCode,
            List<String> processTypeCode) {
        return processInstanceRepo.findCustomerTransactionHistory(customerCode, processTypeCode);
    }

    @Override
    public void rollback(StartResponse response) {
        String processInstanceCode = response.getProcessInstanceCode();
        String camundaProcessInstanceId = response.getProcessInstanceId();

        log.info("[Rollback] Starting rollback for processInstanceCode={}, camundaId={}",
                processInstanceCode, camundaProcessInstanceId);

        // 1. XÓA CAMUNDA PROCESS INSTANCE TRƯỚC
        // Điều này sẽ ngừng tất cả listeners và tasks đang chạy
        deleteCamundaProcessInstance(camundaProcessInstanceId);

        // 2. Xóa data - mỗi repository method đã có @Transactional
        try {
            taskCommentRepository.deleteByProcessInstanceCode(processInstanceCode);
            log.debug("[Rollback] Deleted task comments");
        } catch (Exception e) {
            log.warn("[Rollback] Failed to delete task comments: {}", e.getMessage());
        }

        try {
            taskInboxRepository.deleteByProcessInstanceCode(processInstanceCode);
            log.debug("[Rollback] Deleted task inbox");
        } catch (Exception e) {
            log.warn("[Rollback] Failed to delete task inbox: {}", e.getMessage());
        }

        try {
            docFileRepository.deleteByProcessInstanceCode(processInstanceCode);
            log.debug("[Rollback] Deleted doc files");
        } catch (Exception e) {
            log.warn("[Rollback] Failed to delete doc files: {}", e.getMessage());
        }

        try {
            auditRepository.deleteByProcessInstanceCode(processInstanceCode);
            log.debug("[Rollback] Deleted audits");
        } catch (Exception e) {
            log.warn("[Rollback] Failed to delete audits: {}", e.getMessage());
        }

        try {
            processInstanceRepo.deleteByProcessInstanceCode(processInstanceCode);
            log.debug("[Rollback] Deleted process instance");
        } catch (Exception e) {
            log.warn("[Rollback] Failed to delete process instance: {}", e.getMessage());
        }

        log.info("[Rollback] Completed rollback for processInstanceCode={}", processInstanceCode);
    }

    /**
     * Xóa Camunda process instance để ngừng tất cả hoạt động
     */
    private void deleteCamundaProcessInstance(String processInstanceId) {
        if (processInstanceId == null || processInstanceId.isEmpty()) {
            log.warn("[Rollback] Camunda processInstanceId is null/empty, skipping Camunda deletion");
            return;
        }

        try {
            // Kiểm tra xem process instance còn tồn tại không
            var instance = processEngine.getRuntimeService()
                    .createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (instance != null) {
                // Xóa process instance (cascade delete tất cả tasks, variables, etc.)
                processEngine.getRuntimeService().deleteProcessInstance(
                        processInstanceId,
                        "Rollback: Business service failed after process start");
                log.info("[Rollback] Deleted Camunda process instance: {}", processInstanceId);
            } else {
                log.info("[Rollback] Camunda process instance already ended: {}", processInstanceId);
            }
        } catch (Exception e) {
            log.warn("[Rollback] Failed to delete Camunda process instance {}: {}",
                    processInstanceId, e.getMessage());
            // Tiếp tục xóa data trong DB dù có lỗi
        }
    }
}