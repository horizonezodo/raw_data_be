package ngvgroup.com.bpm.core.base.controller;

import java.util.List;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.core.base.dto.*;
import ngvgroup.com.bpm.features.transaction.dto.CustomerTransactionHistoryDto;
import org.camunda.bpm.engine.rest.dto.runtime.ProcessInstanceDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ngvgroup.bpm.core.common.dto.ResponseData;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.core.base.service.BpmService;

@RestController
@RequestMapping("/bpm")
@RequiredArgsConstructor
public class BpmController {
    private final BpmService service;

    @GetMapping("/detail/{taskId}")
    ResponseEntity<ResponseData<TaskViewBpmData>> getDetail(@PathVariable String taskId) {
        return ResponseData.okEntity(service.getDetail(taskId));
    }

    @GetMapping("/detail-reference-code/{referenceCode}")
    ResponseEntity<ResponseData<List<ProcessFileDto>>> getProcessFilesFromReferenceCode(
            @PathVariable String referenceCode, @RequestParam String processTypeCode) {
        return ResponseData.okEntity(service.getProcessFilesFromReferenceCode(referenceCode, processTypeCode));
    }

    @PostMapping("/claim-task/{taskId}")
    ResponseEntity<ResponseData<Void>> claimTask(@PathVariable("taskId") String taskId) {
        service.claimTask(taskId);
        return ResponseData.okEntity();
    }

    @PostMapping("/save-draft")
    ResponseEntity<ResponseData<Void>> saveDraft(@RequestBody DraftTaskBpmData request) {
        service.saveDraft(request);
        return ResponseData.okEntity();
    }

    @GetMapping("/file-detail/{fileId}")
    ResponseEntity<ResponseData<FileDto>> getFileDetail(@PathVariable String fileId) {
        return ResponseData.okEntity(service.getFileDetail(fileId));
    }

    @GetMapping("/task-definition-key/{taskId}")
    ResponseEntity<ResponseData<String>> getTaskDefinitionKeyFromCamunda(@PathVariable String taskId) {
        return ResponseData.okEntity(service.getTaskDefinitionKeyFromCamunda(taskId));
    }

    @GetMapping("/transaction-history/{customerCode}")
    public ResponseEntity<ResponseData<List<CustomerTransactionHistoryDto>>> getCustomerTransactionHistory(
            @PathVariable String customerCode,
            @RequestParam List<String> processTypeCode) {
        return ResponseData
                .okEntity(service.getCustomerTransactionHistory(customerCode, processTypeCode));
    }

    @PostMapping("/process/start/{key}")
    public ResponseEntity<ResponseData<ProcessInstanceDto>> startProcess(
            @PathVariable("key") String processDefinitionKey,
            @RequestBody StartProcessRequestDto requestBody) {
        try {
            return ResponseData.okEntity(service.startProcess(processDefinitionKey, requestBody));
        } catch (BusinessException e) {
            HttpStatus status = HttpStatus.resolve(e.getErrorCode());
            if (status == null) {
                status = HttpStatus.BAD_REQUEST;
            }
            return ResponseData.errorEntity(e.getErrorCode(), e.getMessage(), status);
        }
    }

    @PostMapping("/process/rollback")
    ResponseEntity<ResponseData<Void>> rollback(@RequestBody StartResponse response) {
        service.rollback(response);
        return ResponseData.okEntity();
    }
}
