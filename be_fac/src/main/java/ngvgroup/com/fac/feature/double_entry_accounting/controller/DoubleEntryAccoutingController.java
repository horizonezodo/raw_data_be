package ngvgroup.com.fac.feature.double_entry_accounting.controller;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.client.dto.response.TaskViewResponse;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.fac.feature.double_entry_accounting.bpm.DoubleEntryRegisterStater;
import ngvgroup.com.fac.feature.double_entry_accounting.bpm.handler.RegisterApproveHandler;
import ngvgroup.com.fac.feature.double_entry_accounting.bpm.handler.RegisterEditHandler;
import ngvgroup.com.fac.feature.double_entry_accounting.dto.DoubleEntryAccountingProcessDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("double-entry-acct")
@AllArgsConstructor
public class DoubleEntryAccoutingController {

    private DoubleEntryRegisterStater doubleEntryRegisterStater;
    private final FileService fileService;
    private RegisterApproveHandler registerApproveHandler;
    private RegisterEditHandler registerEditHandler;

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileId") String fileId) {
        return fileService.downloadFileById(fileId);
    }

    @GetMapping("/edit-detail")
    public ResponseEntity<ResponseData<TaskViewResponse<DoubleEntryAccountingProcessDto>>> getDetail(@RequestParam String taskId){
        return ResponseData.okEntity(registerEditHandler.getTaskDetail(taskId));
    }

    @Operation(description = "Lấy detail đơn để phê duyệt hoặc từ chối")
    @GetMapping("/approval-detail")
    public ResponseEntity<ResponseData<TaskViewResponse<DoubleEntryAccountingProcessDto>>> getApprovalDetail(
            @RequestParam String taskId) {
        return ResponseData.okEntity(registerApproveHandler.getTaskDetail(taskId));
    }

}
