package ngvgroup.com.rpt.features.ctgcfgworkflow.controller;

import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgworkflow.service.CtgCfgWorkflowTransitionService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workflow-transition")
public class CtgCfgWorkflowTransitionController {
    private final CtgCfgWorkflowTransitionService service;

    @LogActivity(function = "Xuất danh sách hành động")
    @Operation(summary = "Xuất danh sách hành động")
    @PostMapping("/export-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(
            @RequestParam List<String> labels,
            @RequestParam String keyword,
            @RequestParam String fileName,
            @RequestParam String workflowCode) {
        return service.exportToExcel(labels, keyword, fileName, workflowCode);
    }
}