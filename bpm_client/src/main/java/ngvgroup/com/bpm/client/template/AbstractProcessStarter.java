package ngvgroup.com.bpm.client.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import feign.FeignException;
import ngvgroup.com.bpm.client.dto.shared.ComCfgProcessFileDto;
import ngvgroup.com.bpm.client.exception.BpmClientErrorCode;
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
import ngvgroup.com.bpm.client.dto.shared.FileDto;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;
import ngvgroup.com.bpm.client.dto.shared.ReportReqDto;
import ngvgroup.com.bpm.client.dto.shared.TemplateResDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.dto.variable.TaskBpmData;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.response.StartResponse;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.feign.CommonFeignClient;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.dto.camunda.ProcessInstanceDto;
import ngvgroup.com.bpm.client.dto.camunda.StartProcessRequestDto;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.bpm.client.utils.GenericAuditValidator;
import ngvgroup.com.bpm.client.constant.VariableConstants;

@RequiredArgsConstructor
public abstract class AbstractProcessStarter<BusinessDto> {

    protected final BpmFeignClient bpmFeignClient;
    protected final FileService fileService;
    protected final CommonFeignClient commonFeignClient;
    protected final DocumentTemplateService templateService;

    public StartResponse startProcess(StartRequest<BusinessDto> dto,
            MultiValueMap<String, MultipartFile> multiFileMap) {
        // 1. Validate nghiệp vụ đặc thù
        validateSpecificLogic(dto.getBusinessData());

        // 2. Validate Audit
        validateAudit(dto.getBusinessData(), dto.getBpmData().getTaskAudits());

        // 3. Generate ProcessInstanceCode
        String processInstanceCode = generateAndAttachToBusinessData(dto);

        // 4. Attach File Nghiệp vụ (VD: CRM phải attach file Đơn ĐKKH và Đơn ĐKKH điều
        // chỉnh)
        String attachedProcessCode = attachBusinessFile(dto.getBusinessData(), multiFileMap);

        // 5. Xử lý file (Template, Upload, Merge)
        List<ProcessFileDto> fileData = processFiles(attachedProcessCode, processInstanceCode,
                dto.getBpmData().getExistingProcessFiles(),
                multiFileMap, dto.getBpmData().getProcessDefinitionKey());

        // 6. Chuẩn bị biến Camunda
        Map<String, Object> variables = prepareCamundaVariables(dto, fileData);

        // 7. Gọi BPM
        StartResponse response = callBpmStartProcess(processInstanceCode, variables,
                dto.getBpmData().getProcessDefinitionKey());

        // 8. Lưu dữ liệu nghiệp vụ
        try {
            saveBusinessData(dto.getBpmData().getProcessDefinitionKey(),
                    dto.getBusinessData());
        } catch (Exception e) {
            bpmFeignClient.rollback(response);
            throw e;
        }

        return response;
    }

    protected abstract String generateAndAttachToBusinessData(StartRequest<BusinessDto> dto);

    protected abstract AttachmentContext createAttachmentContext(BusinessDto businessData);

    protected abstract void validateSpecificLogic(BusinessDto businessData);

    protected abstract void saveBusinessData(String processDefinitionKey, BusinessDto businessData);

    @SuppressWarnings("unchecked")
    public Class<BusinessDto> getBusinessClass() {
        return (Class<BusinessDto>) GenericTypeResolver.resolveTypeArgument(getClass(), AbstractProcessStarter.class);
    }

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

    protected abstract ProcessData prepareProcessData(StartRequest<BusinessDto> dto);

    private void validateAudit(BusinessDto businessData, List<AuditDto> audits) {
        GenericAuditValidator.validate(businessData, audits);
    }

    private List<ProcessFileDto> processFiles(String attachedProcessFileCode, String processInstanceCode,
            List<ProcessFileDto> existingProcessFiles, MultiValueMap<String, MultipartFile> multiFileMap,
            String processTypeCode) {
        validateProcessFiles(processTypeCode, existingProcessFiles, multiFileMap);

        if (CollectionUtils.isEmpty(multiFileMap)) {
            return existingProcessFiles;
        }

        Map<String, List<FileDto>> fileData = new HashMap<>();

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

    private Map<String, Object> prepareCamundaVariables(StartRequest<BusinessDto> dto,
            List<ProcessFileDto> fileData) {
        // 1. Prepare Process Data (Abstract)
        ProcessData processData = prepareProcessData(dto);

        // 2. Prepare Task Bpm Data (Generic)
        TaskBpmData taskBpmData = new TaskBpmData();
        taskBpmData.setCurrentUser(BpmClientSecurityUtils.getCurrentUserName());
        taskBpmData.setTaskComment(dto.getBpmData().getTaskComment());

        if (fileData != null && !CollectionUtils.isEmpty(fileData)) {
            taskBpmData.setTaskProcessFiles(fileData);
        }

        if (dto.getBpmData().getTaskAudits() != null && !CollectionUtils.isEmpty(dto.getBpmData().getTaskAudits())) {
            taskBpmData.setTaskAudits(dto.getBpmData().getTaskAudits());
        }

        // 3. Build Map
        Map<String, Object> variables = new HashMap<>();
        variables.put(VariableConstants.BUSINESS_DATA_VARIABLE, dto.getBusinessData());
        variables.put(VariableConstants.TASK_BPM_DATA_VARIABLE, taskBpmData);
        variables.put(VariableConstants.TENANT_ID_VARIABLE, TenantContext.getTenantId());
        variables.put(VariableConstants.PROCESS_DATA_VARIABLE, processData);

        return variables;
    }

    private StartResponse callBpmStartProcess(String processInstanceCode, Map<String, Object> variables,
            String processDefinitionKey) {
        StartProcessRequestDto request = StartProcessRequestDto.builder()
                .businessKey(processInstanceCode)
                .userId(BpmClientSecurityUtils.getCurrentUserId())
                .variables(variables)
                .build();
        try {
            ProcessInstanceDto instance = bpmFeignClient.startProcess(processDefinitionKey, request).getData();
            return new StartResponse(processInstanceCode, instance.getId());
        } catch (FeignException e) {
            if (e.status() == 401) {
                throw new BusinessException(BpmClientErrorCode.RESOURCE_MAPPING_NOT_FOUND);
            }
            throw e;
        }
    }
}
