package ngvgroup.com.fac.feature.single_entry_acct.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.dto.request.DraftTaskBpmRequest;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.response.StartResponse;
import ngvgroup.com.bpm.client.dto.response.TaskViewResponse;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.utils.JsonUtil;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.common.service.impl.JsonParseService;
import ngvgroup.com.fac.feature.single_entry_acct.bpm.SingleEntryAcctStarter;
import ngvgroup.com.fac.feature.single_entry_acct.bpm.handler.SingleEntryAcctApproveHandler;
import ngvgroup.com.fac.feature.single_entry_acct.bpm.handler.SingleEntryAcctEditHandler;
import ngvgroup.com.fac.feature.single_entry_acct.dto.SingleEntryAcctDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/entry-process")
@RequiredArgsConstructor
public class SingleEntryAcctController {

    private final FileService fileService;
    private final SingleEntryAcctStarter starter;
    private final SingleEntryAcctApproveHandler approveHandler;
    private final SingleEntryAcctEditHandler editHandler;
    private final JsonParseService jsonParseService;

    @Operation(description = "Khởi tạo bút toán lẻ")
    @PostMapping(value = "/start-process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<StartResponse>> startProcess(
            @RequestPart("data") String dtoJson,
            MultipartHttpServletRequest request) {
        StartRequest<SingleEntryAcctDTO> dto = jsonParseService.parseJson(dtoJson,
                new TypeReference<>() {
                });
        dto.getBpmData().setProcessDefinitionKey(FacVariableConstants.PREFIX_ACCOUNT_ENTRY_INIT);

        return ResponseData.okEntity(starter.startProcess(dto, request.getMultiFileMap()));
    }

    @Operation(description = "Lấy detail đơn để phê duyệt hoặc từ chối")
    @GetMapping("/approval-detail/{taskId}")
    public ResponseEntity<ResponseData<TaskViewResponse<SingleEntryAcctDTO>>> getApprovalDetail(
            @PathVariable String taskId) {
        return ResponseData.okEntity(approveHandler.getTaskDetail(taskId));
    }

    @Operation(description = "Lấy detail đơn để hoàn thành hoặc hủy")
    @GetMapping("/edit-detail/{taskId}")
    public ResponseEntity<ResponseData<TaskViewResponse<SingleEntryAcctDTO>>> getEditDetail(
            @PathVariable String taskId) {
        return ResponseData.okEntity(editHandler.getTaskDetail(taskId));
    }

    @Operation(description = "Submit bước chỉnh sửa (Hoàn thành task Edit)")
    @PostMapping(value = "/submit-edit-task", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<Void>> submitEditTask(
            @RequestPart("data") String dtoJson,
            MultipartHttpServletRequest request) {
        SubmitTaskRequest<SingleEntryAcctDTO> dto = JsonUtil.parseJson(dtoJson,
                new TypeReference<>() {});
        editHandler.submitTask(dto, request.getMultiFileMap());
        return ResponseData.okEntity();
    }

    @Operation(description = "Submit bước phê duyệt")
    @PostMapping(value = "/submit-approve-task", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<Void>> submitApproveTask(
            @RequestPart("data") String dtoJson,
            MultipartHttpServletRequest request) {
        SubmitTaskRequest<SingleEntryAcctDTO> dto = JsonUtil.parseJson(dtoJson,
                new TypeReference<>() {
                });
        approveHandler.submitTask(dto, request.getMultiFileMap());
        return ResponseData.okEntity();
    }

    @Operation(description = "Lưu tạm")
    @PostMapping(value = "/save-draft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<Void>> saveDraft(
            @RequestPart("data") String dtoJson,
            @RequestParam("taskId") String taskId,
            MultipartHttpServletRequest request) {
        DraftTaskBpmRequest<SingleEntryAcctDTO> dto = JsonUtil.parseJson(dtoJson,
                new TypeReference<>() {
                });

        editHandler.saveDraft(dto, request.getMultiFileMap());
        return ResponseData.okEntity();
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileId") String fileId) {
        return fileService.downloadFileById(fileId);
    }
}
