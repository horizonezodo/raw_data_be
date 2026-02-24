package ngvgroup.com.rpt.features.ctgcfgworkflow.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.CtgCfgWorkflowWithTransitionsDto;
import ngvgroup.com.rpt.features.ctgcfgworkflow.service.CtgCfgWorkflowWithTransitionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflow-complete")
public class CtgCfgWorkflowWithTransitionsController {
    private final CtgCfgWorkflowWithTransitionsService service;

    @LogActivity(function = "Tạo mới quy trình đầy đủ")
    @Operation(summary = "Tạo mới quy trình với đầy đủ transitions, conditions và post-functions")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<CtgCfgWorkflowWithTransitionsDto>> createWorkflowWithTransitions(
            @RequestBody CtgCfgWorkflowWithTransitionsDto dto) {
        CtgCfgWorkflowWithTransitionsDto result = service.createWorkflowWithTransitions(dto);
        return ResponseData.okEntity(result);
    }

    @LogActivity(function = "Cập nhật quy trình đầy đủ")
    @Operation(summary = "Cập nhật quy trình với đầy đủ transitions, conditions và post-functions")
    @PutMapping("/update/{workflowCode}")
    public ResponseEntity<ResponseData<CtgCfgWorkflowWithTransitionsDto>> updateWorkflowWithTransitions(
            @RequestBody CtgCfgWorkflowWithTransitionsDto dto,
            @PathVariable("workflowCode") String workflowCode) {
        CtgCfgWorkflowWithTransitionsDto result = service.updateWorkflowWithTransitions(dto, workflowCode);
        return ResponseData.okEntity(result);
    }

    @LogActivity(function = "Lấy quy trình đầy đủ")
    @Operation(summary = "Lấy thông tin quy trình với đầy đủ transitions")
    @GetMapping("/get/{workflowCode}")
    public ResponseEntity<ResponseData<CtgCfgWorkflowWithTransitionsDto>> getWorkflowWithTransitions(
            @PathVariable("workflowCode") String workflowCode) {
        CtgCfgWorkflowWithTransitionsDto result = service.getWorkflowWithTransitions(workflowCode);
        return ResponseData.okEntity(result);
    }

    @LogActivity(function = "Kiểm tra hành động quy trình tồn tại")
    @Operation(summary = "Kiểm tra tồn tại mã hành động và mã quy trình")
    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String workflowCode, @RequestParam String transitionCode) {
        return ResponseData.okEntity(service.existsByWorkflowCodeAndTransitionCodeAndIsDeleted(workflowCode, transitionCode));
    }

}
