package ngvgroup.com.bpm.client.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.client.dto.request.DraftTaskBpmRequest;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.response.StartResponse;
import ngvgroup.com.bpm.client.dto.response.TaskViewResponse;
import ngvgroup.com.bpm.client.dto.shared.ComCfgProcessFileDto;
import ngvgroup.com.bpm.client.dto.shared.ReportReqDto;
import ngvgroup.com.bpm.client.service.BpmClientService;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.template.ProcessStarterService;
import ngvgroup.com.bpm.client.template.UserTaskService;
import ngvgroup.com.bpm.client.utils.JsonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * Generic BPM Process Controller that handles all process and task operations.
 * <p>
 * This controller uses {@link ProcessStarterService} and
 * {@link UserTaskService}
 * to dynamically route requests to the appropriate handlers based on
 * processDefinitionKey and taskDefinitionKey.
 * <p>
 * Services using this library can enable this controller by setting:
 * 
 * <pre>
 * bpm.client.controller.enabled = true
 * </pre>
 *
 * @see ProcessStarterService
 * @see UserTaskService
 */
@Slf4j
@RestController
@RequestMapping("${bpm.client.controller.base-path:/bpm-client}")
@RequiredArgsConstructor
@Tag(name = "BPM Process", description = "Generic API for BPM process and task operations")
@ConditionalOnProperty(name = "bpm.client.controller.enabled", havingValue = "true", matchIfMissing = true)
public class BpmClientController {

    private final ProcessStarterService processStarterService;
    private final UserTaskService userTaskService;
    private final FileService fileService;
    private final BpmClientService bpmClientService;

    // ==================== PROCESS FILES ====================

    @Operation(summary = "Get process files config", description = "Get process files configuration by process type code.")
    @GetMapping("/process-files/{processTypeCode}")
    public ResponseEntity<ResponseData<List<ComCfgProcessFileDto>>> getProcessFilesDetailByProcessTypeCode(
            @PathVariable String processTypeCode) {
        return ResponseData.okEntity(bpmClientService.getProcessFilesDetailByProcessTypeCode(processTypeCode));
    }

    // ==================== PROCESS START ====================

    @Operation(summary = "Start a new process", description = "Start a new BPM process. The processDefinitionKey in the request determines which handler to use.")
    @PostMapping(value = "/process/start", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<StartResponse>> startProcess(
            @RequestPart("data") String dtoJson,
            MultipartHttpServletRequest request) {

        StartRequest<Map<String, Object>> dto = JsonUtil.parseJsonWithGeneric(dtoJson, StartRequest.class, Map.class);
        StartResponse response = processStarterService.startProcess(dto, request.getMultiFileMap());

        return ResponseData.okEntity(response);
    }

    // ==================== TASK DETAIL ====================

    @Operation(summary = "Get task detail", description = "Get task details for viewing/editing. The handler is determined by taskDefinitionKey from Camunda.")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<ResponseData<TaskViewResponse<?>>> getTaskDetail(
            @PathVariable String taskId) {

        TaskViewResponse<?> response = userTaskService.getTaskDetail(taskId);
        return ResponseData.okEntity(response);
    }

    // ==================== TASK SUBMIT ====================

    @Operation(summary = "Submit/complete a task", description = "Submit a task with form data. The handler is determined by taskDefinitionKey from Camunda.")
    @PostMapping(value = "/task/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<Void>> submitTask(
            @RequestPart("data") String dtoJson,
            MultipartHttpServletRequest request) {

        SubmitTaskRequest<Map<String, Object>> dto = JsonUtil.parseJsonWithGeneric(dtoJson, SubmitTaskRequest.class,
                Map.class);
        userTaskService.submitTask(dto, request.getMultiFileMap());

        return ResponseData.okEntity();
    }

    // ==================== SAVE DRAFT ====================

    @Operation(summary = "Save task as draft", description = "Save task data without completing. The handler is determined by taskDefinitionKey from Camunda.")
    @PostMapping(value = "/task/save-draft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<Void>> saveDraft(
            @RequestPart("data") String dtoJson,
            MultipartHttpServletRequest request) {

        DraftTaskBpmRequest<Map<String, Object>> dto = JsonUtil.parseJsonWithGeneric(dtoJson, DraftTaskBpmRequest.class,
                Map.class);
        userTaskService.saveDraft(dto, request.getMultiFileMap());

        return ResponseData.okEntity();
    }

    // ==================== FILE DOWNLOAD ====================

    @Operation(summary = "Download attachment", description = "Download a file attachment by fileId.")
    @PostMapping("/file/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileId") String fileId) {
        return fileService.downloadFileById(fileId);
    }

    @PostMapping("/file/generate-report")
    public ResponseEntity<byte[]> generateReport(@RequestBody ReportReqDto reportReqDto) {
        return bpmClientService.generateReport(reportReqDto);
    }
}
