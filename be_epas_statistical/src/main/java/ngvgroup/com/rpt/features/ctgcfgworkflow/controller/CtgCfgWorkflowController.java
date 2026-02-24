package ngvgroup.com.rpt.features.ctgcfgworkflow.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.CtgCfgWorkflowDto;
import ngvgroup.com.rpt.features.ctgcfgworkflow.service.CtgCfgWorkflowService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflow")
public class CtgCfgWorkflowController {
    private final CtgCfgWorkflowService service;

    @LogActivity(function = "Lấy danh sách quy trình")
    @Operation(summary = "Lấy danh sách quy trình - phân trang")
    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgWorkflowDto>>> pageWorkflow(@RequestParam String keyword,
            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(service.pageWorkflow(keyword, pageable));
    }

    @LogActivity(function = "Lấy chi tiết quy trình")
    @Operation(summary = "Lấy thông tin chi tiết quy trình")
    @GetMapping("/get/{workflowCode}")
    public ResponseEntity<ResponseData<CtgCfgWorkflowDto>> getDetail(
            @PathVariable("workflowCode") String workflowCode) {
        return ResponseData.okEntity(service.getDetail(workflowCode));
    }

    @LogActivity(function = "Xóa quy trình")
    @Operation(summary = "Xóa quy trình")
    @PostMapping("/delete/{workflowCode}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable("workflowCode") String workflowCode) {
        service.deleteWorkflow(workflowCode);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Xuất danh sách quy trình")
    @Operation(summary = "Xuất danh sách quy trình")
    @PostMapping("/export-to-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(@RequestParam List<String> labels,
            @RequestParam String keyword,
            @RequestParam String fileName) {
        return service.exportToExcel(labels, keyword, fileName);
    }

    @LogActivity(function = "Lấy danh sách quy trình")
    @GetMapping("/get-list")
    public ResponseEntity<ResponseData<List<CtgCfgWorkflowDto>>> getList() {
        return ResponseData.okEntity(service.getList());
    }

    @LogActivity(function = "Kiểm tra quy trình tồn tại")
    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String workflowCode) {
        return ResponseData.okEntity(service.existsByWorkflowCode(workflowCode));
    }
}
