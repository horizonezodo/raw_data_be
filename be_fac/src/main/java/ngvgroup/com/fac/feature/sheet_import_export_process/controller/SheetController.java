package ngvgroup.com.fac.feature.sheet_import_export_process.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.response.StartResponse;
import ngvgroup.com.bpm.client.dto.response.TaskViewResponse;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.common.service.impl.JsonParseService;
import ngvgroup.com.fac.feature.sheet_import_export_process.bpm.SheetRegisterStarter;
import ngvgroup.com.fac.feature.sheet_import_export_process.bpm.handler.SheetApproveHandler;
import ngvgroup.com.fac.feature.sheet_import_export_process.bpm.handler.SheetEditHandler;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.SheetInfoDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("process-sheet-import-export")
@RequiredArgsConstructor
@Log4j2
public class SheetController {
    private final SheetRegisterStarter stater;
    private final SheetApproveHandler approveHandler;
    private final SheetEditHandler editHandler;
    private final JsonParseService jsonParseService;
    private final FileService fileService;

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileId") String fileId) {
        return fileService.downloadFileById(fileId);
    }

    @Operation(summary = "Khởi tạo quy trình xuất nhập ngoại bảng")
    @PostMapping(value = "/start-process", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<StartResponse>> startProcess(
            @RequestPart(value = "data") String request,
            MultipartHttpServletRequest multipartRequest
    ) {
        StartRequest<SheetInfoDto> parsed = jsonParseService.parseJson(request, new TypeReference<StartRequest<SheetInfoDto>>() {
        });
        parsed.getBpmData().setProcessDefinitionKey(FacVariableConstants.SHEET_KEY_PROCESS);
        return ResponseData.okEntity(stater.startProcess(parsed, multipartRequest.getMultiFileMap()));
    }

    @Operation(description = "Submit bước phê duyệt")
    @PostMapping(value = "/submit-approve-task", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<Void>> submitApproveTask(
            @RequestPart("data") String dtoJson,
            MultipartHttpServletRequest request) {
        SubmitTaskRequest<SheetInfoDto> dto = jsonParseService.parseJson(dtoJson, new TypeReference<SubmitTaskRequest<SheetInfoDto>>() {
        });
        approveHandler.submitTask(dto, request.getMultiFileMap());
        return ResponseData.okEntity();
    }

    @Operation(description = "Submit bước chỉnh sửa")
    @PostMapping(value = "/submit-edit-task", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<Void>> submitEditTask(
            @RequestPart("data") String dtoJson,
            MultipartHttpServletRequest request) {
        SubmitTaskRequest<SheetInfoDto> dto = jsonParseService.parseJson(dtoJson,
                new TypeReference<SubmitTaskRequest<SheetInfoDto>>() {
                });
        editHandler.submitTask(dto, request.getMultiFileMap());
        return ResponseData.okEntity();
    }

    @Operation(description = "Lấy detail")
    @GetMapping("approve-detail/{taskId}")
    public ResponseEntity<ResponseData<TaskViewResponse<SheetInfoDto>>> getDetail(@PathVariable String taskId) {
        return ResponseData.okEntity(approveHandler.getTaskDetail(taskId));
    }
}
