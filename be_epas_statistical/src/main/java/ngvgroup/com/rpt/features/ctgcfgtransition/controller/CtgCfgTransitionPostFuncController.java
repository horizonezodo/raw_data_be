package ngvgroup.com.rpt.features.ctgcfgtransition.controller;

import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgtransition.service.CtgCfgTransitionPostFuncService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transition-post-func")
public class CtgCfgTransitionPostFuncController {
    private final CtgCfgTransitionPostFuncService service;

    @LogActivity(function = "Xuất danh sách hậu xử lý")
    @Operation(summary = "Xuất danh sách hậu xử lý")
    @PostMapping("/export-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(
            @RequestParam List<String> labels,
            @RequestParam String keyword,
            @RequestParam String fileName,
            @RequestParam String transitionCode) {
        return service.exportToExcel(labels, keyword, fileName, transitionCode);
    }
}
