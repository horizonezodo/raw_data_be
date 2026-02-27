package ngvgroup.com.bpm.client.template;

import java.util.*;

import ngvgroup.com.bpm.client.dto.shared.ComCfgProcessFileDto;
import ngvgroup.com.bpm.client.utils.BpmClientSecurityUtils;

import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.springframework.core.GenericTypeResolver;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.shared.AuditDto;
import ngvgroup.com.bpm.client.dto.shared.BpmMultipartFile;
import ngvgroup.com.bpm.client.dto.request.DraftTaskBpmRequest;
import ngvgroup.com.bpm.client.dto.shared.FileDto;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;
import ngvgroup.com.bpm.client.dto.shared.ReportReqDto;
import ngvgroup.com.bpm.client.dto.shared.TemplateResDto;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.TaskBpmData;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.response.TaskViewBpmData;
import ngvgroup.com.bpm.client.dto.response.TaskViewResponse;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.feign.CommonFeignClient;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.dto.camunda.CompleteTaskRequestDto;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.bpm.client.utils.GenericAuditValidator;
import ngvgroup.com.bpm.client.constant.VariableConstants;

@RequiredArgsConstructor
public abstract class AbstractUserTask<BusinessDto> {
    protected final BpmFeignClient bpmFeignClient;
    protected final FileService fileService;
    protected final CommonFeignClient commonFeignClient;
    protected final DocumentTemplateService templateService;

    public TaskViewResponse<BusinessDto> getTaskDetail(String taskId) {
        // Bước 1: Call MS_BPM lấy TaskViewBpmData
        TaskViewBpmData bpmData = getBpmData(taskId);

        // Bước 2: Lấy dữ liệu nghiệp vụ businessData
        BusinessDto businessData = getBusinessData(bpmData);

        // Bước 3: Merge TaskViewBpmData và businessData thành TaskViewResponse
        return buildResponse(bpmData, businessData);
    }

    public void submitTask(SubmitTaskRequest<BusinessDto> dto, MultiValueMap<String, MultipartFile> multiFileMap) {
        Map<String, Object> variables;
        if (Objects.equals(dto.getBpmData().getTaskStatus(), VariableConstants.SEND_APPROVE)) {
            // Validate nghiệp vụ đặc thù
            validateSpecificLogic(dto.getBusinessData());
            // Validate Audit
            validateAudit(dto.getBusinessData(), dto.getBpmData().getTaskAudits());
            // Nghiệp vụ attach File (Sinh Template nếu cần)
            String attachedProcessFileCode = attachBusinessFile(dto.getBusinessData(), multiFileMap);
            // Xử lý file (Tách biệt Merge và Upload)
            List<ProcessFileDto> fileData = processFiles(attachedProcessFileCode,
                    dto.getBpmData().getProcessInstanceCode(),
                    dto.getBpmData().getExistingProcessFiles(), multiFileMap, getProcessTypeCode());
            variables = prepareCamundaVariables(dto, fileData);
        } else {
            variables = updateCamundaVariables(dto);
        }
        // Gọi BPM Complete Task
        callBpmCompleteTask(dto.getBpmData().getTaskId(), variables);
    }

    private Map<String, Object> updateCamundaVariables(SubmitTaskRequest<BusinessDto> dto) {
        // Fetch variables from Camunda native REST API
        Map<String, ngvgroup.com.bpm.client.dto.camunda.CamundaVariableDto> camundaVariables = bpmFeignClient
                .getTaskVariablesFromCamunda(dto.getBpmData().getTaskId());

        // Extract TaskBpmData
        ngvgroup.com.bpm.client.dto.camunda.CamundaVariableDto taskBpmDataVar = camundaVariables
                .get(VariableConstants.TASK_BPM_DATA_VARIABLE);
        TaskBpmData taskBpmData = CamundaVariablesUtil.getTypedVariable(taskBpmDataVar, TaskBpmData.class);

        if (taskBpmData == null) {
            taskBpmData = new TaskBpmData(); // Fallback if missing
        }

        if (dto.getBpmData().getTaskStatus().equalsIgnoreCase(VariableConstants.REJECT)) {
            // 2. Mail Variable (Abstract)
            MailVariableDto mailDto = prepareMailVariable(dto);
            if (mailDto != null) {
                taskBpmData.setMailVariable(mailDto);
            }
        }

        // Update values
        taskBpmData.setCurrentUser(BpmClientSecurityUtils.getCurrentUserName());
        taskBpmData.setTaskStatus(dto.getBpmData().getTaskStatus());
        taskBpmData.setTaskComment(dto.getBpmData().getTaskComment());
        taskBpmData.setTaskAudits(dto.getBpmData().getTaskAudits());
        taskBpmData.setTaskProcessFiles(dto.getBpmData().getExistingProcessFiles());

        // Prepare return map
        Map<String, Object> variables = new HashMap<>();
        variables.put(VariableConstants.APPROVAL_RESULT_VARIABLE, dto.getBpmData().getTaskStatus());
        variables.put(VariableConstants.TASK_BPM_DATA_VARIABLE, taskBpmData);
        return variables;
    }

    public void saveDraft(DraftTaskBpmRequest<BusinessDto> dto, MultiValueMap<String, MultipartFile> multiFileMap) {
        validateSpecificLogic(dto.getBusinessData());

        // 2. Validate Audit
        validateAudit(dto.getBusinessData(), dto.getBpmData().getTaskAudits());

        // 3. Lưu dữ liệu nghiệp vụ (Update DB)

        // 4. Nghiệp vụ attach File (Sinh Template nếu cần)
        String attachedProcessFileCode = attachBusinessFile(dto.getBusinessData(), multiFileMap);

        List<ProcessFileDto> fileData = processFiles(attachedProcessFileCode, dto.getBpmData().getProcessInstanceCode(),
                dto.getBpmData().getExistingProcessFiles(), multiFileMap, getProcessTypeCode());

        dto.getBpmData().setExistingProcessFiles(fileData);
        bpmFeignClient.saveDraft(dto.getBpmData());

        saveDraftBusinessData(dto.getBusinessData());
    }

    protected abstract void saveDraftBusinessData(BusinessDto businessData);

    @SuppressWarnings("unchecked")
    public Class<BusinessDto> getBusinessClass() {
        return (Class<BusinessDto>) GenericTypeResolver.resolveTypeArgument(getClass(), AbstractUserTask.class);
    }

    protected abstract void validateSpecificLogic(BusinessDto businessData);

    protected abstract String getProcessTypeCode();

    private String attachBusinessFile(BusinessDto businessData,
            MultiValueMap<String, MultipartFile> multiFileMap) {

        AttachmentContext context = createAttachmentContext(businessData);
        if (context == null) {
            return "";
        }

        multiFileMap.remove(context.getProcessFileCode());

        TemplateResDto template = commonFeignClient.getDetail(context.getProcessFileCode()).getData();

        ReportReqDto reportReqDto = new ReportReqDto();
        reportReqDto.setTemplateCode(context.getProcessFileCode());
        reportReqDto.setDataSource(context.getContext());

        String fileName = template.getFileName();
        if (fileName != null && fileName.contains(".")) {
            reportReqDto.setFormat(fileName.substring(fileName.lastIndexOf(".") + 1));
        }

        byte[] fileContent = commonFeignClient.generateReport(reportReqDto).getBody();

        MultipartFile docxFile = new BpmMultipartFile(template.getFileName(), template.getFileName(),
                MediaType.APPLICATION_OCTET_STREAM_VALUE, fileContent);

        multiFileMap.add(context.getProcessFileCode(), docxFile);

        return context.getProcessFileCode();
    }

    protected abstract AttachmentContext createAttachmentContext(BusinessDto businessData);

    protected abstract MailVariableDto prepareMailVariable(SubmitTaskRequest<BusinessDto> dto);

    protected abstract BusinessDto getBusinessData(TaskViewBpmData bpmData);

    private void validateAudit(BusinessDto businessData, List<AuditDto> taskAudits) {
        GenericAuditValidator.validate(businessData, taskAudits);
    }

    private List<ProcessFileDto> processFiles(String attachedProcessFileCode, String processInstanceCode,
            List<ProcessFileDto> existingProcessFiles, MultiValueMap<String, MultipartFile> multiFileMap,
            String processTypeCode) {
        validateProcessFiles(processTypeCode, existingProcessFiles, multiFileMap);
        if (CollectionUtils.isEmpty(multiFileMap)) {
            return existingProcessFiles;
        }

        Map<String, List<FileDto>> fileData = new HashMap<>();

        if (!CollectionUtils.isEmpty(multiFileMap)) {
            for (Map.Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
                String folder = "%s/%s/%s".formatted(processInstanceCode, UUID.randomUUID(), entry.getKey());

                List<MultipartFile> files = entry.getValue();
                if (CollectionUtils.isEmpty(files))
                    continue;

                List<FileDto> uploadedFiles = new ArrayList<>();
                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        FileDto fileDto = fileService.uploadFile(folder, file);
                        uploadedFiles.add(fileDto);
                    }
                }

                if (!uploadedFiles.isEmpty()) {
                    fileData.put(entry.getKey(), uploadedFiles);
                }
            }
        }

        if (!CollectionUtils.isEmpty(existingProcessFiles)) {
            for (var processFile : existingProcessFiles) {
                if (attachedProcessFileCode.equalsIgnoreCase(processFile.getProcessFileCode()))
                    continue;
                fileData.computeIfAbsent(processFile.getProcessFileCode(), k -> new ArrayList<>())
                        .addAll(processFile.getFiles());
            }
        }

        // Convert Map to List<ProcessFileDto>
        if (fileData.isEmpty()) {
            return existingProcessFiles;
        }

        List<ProcessFileDto> result = new ArrayList<>();
        for (Map.Entry<String, List<FileDto>> entry : fileData.entrySet()) {
            ProcessFileDto processFileDto = new ProcessFileDto();
            processFileDto.setProcessFileCode(entry.getKey());
            processFileDto.setFiles(entry.getValue());
            result.add(processFileDto);
        }

        return result;
    }

    private void validateProcessFiles(String processTypeCode, List<ProcessFileDto> existingProcessFiles,
            MultiValueMap<String, MultipartFile> multiFileMap) {
        List<ComCfgProcessFileDto> cfgProcessFiles = bpmFeignClient.getProcessFiles(processTypeCode).getData();
        if (CollectionUtils.isEmpty(cfgProcessFiles)) {
            return;
        }

        for (ComCfgProcessFileDto cfgFile : cfgProcessFiles) {
            if ("001".equals(cfgFile.getFileTypeCode())) { // 001 is mandatory
                boolean isFileProvided = false;
                // Check in existing files
                if (!CollectionUtils.isEmpty(existingProcessFiles)) {
                    if (existingProcessFiles.stream().anyMatch(
                            f -> f.getProcessFileCode().equals(cfgFile.getProcessFileCode())
                                    && !CollectionUtils.isEmpty(f.getFiles()))) {
                        isFileProvided = true;
                    }
                }
                // Check in new files if not found in existing
                if (!isFileProvided && multiFileMap != null) {
                    if (multiFileMap.containsKey(cfgFile.getProcessFileCode())
                            && !CollectionUtils.isEmpty(multiFileMap.get(cfgFile.getProcessFileCode()))) {
                        isFileProvided = true;
                    }
                }

                if (!isFileProvided) {
                    throw new BusinessException(ErrorCode.BAD_REQUEST,
                            "File bắt buộc chưa được tải lên: " + cfgFile.getProcessFileName());
                }
            }
        }
    }

    private Map<String, Object> prepareCamundaVariables(SubmitTaskRequest<BusinessDto> dto,
            List<ProcessFileDto> fileData) {
        // 1. Task Bpm Data
        TaskBpmData taskBpmData = new TaskBpmData();
        taskBpmData.setCurrentUser(BpmClientSecurityUtils.getCurrentUserName());
        taskBpmData.setTaskStatus(dto.getBpmData().getTaskStatus());
        taskBpmData.setTaskComment(dto.getBpmData().getTaskComment());

        if (dto.getBpmData().getTaskAudits() != null && !CollectionUtils.isEmpty(dto.getBpmData().getTaskAudits())) {
            taskBpmData.setTaskAudits(dto.getBpmData().getTaskAudits());
        }

        if (fileData != null && !CollectionUtils.isEmpty(fileData)) {
            taskBpmData.setTaskProcessFiles(fileData);
        }

        // 2. Build Map
        Map<String, Object> variables = new HashMap<>();
        variables.put(VariableConstants.BUSINESS_DATA_VARIABLE, dto.getBusinessData());
        variables.put(VariableConstants.TASK_BPM_DATA_VARIABLE, taskBpmData);
        variables.put(VariableConstants.APPROVAL_RESULT_VARIABLE, dto.getBpmData().getTaskStatus());

        return variables;
    }

    private void callBpmCompleteTask(String taskId, Map<String, Object> variables) {
        CompleteTaskRequestDto request = CompleteTaskRequestDto.builder()
                .variables(CamundaVariablesUtil.toCamundaMap(variables))
                .build();
        bpmFeignClient.completeTask(taskId, request);
    }

    private TaskViewBpmData getBpmData(String taskId) {
        return bpmFeignClient.getDetail(taskId).getData();
    }

    private TaskViewResponse<BusinessDto> buildResponse(TaskViewBpmData bpmData, BusinessDto businessData) {
        TaskViewResponse<BusinessDto> response = new TaskViewResponse<>();
        response.setBpmData(bpmData);
        response.setBusinessData(businessData);
        return response;
    }
}
