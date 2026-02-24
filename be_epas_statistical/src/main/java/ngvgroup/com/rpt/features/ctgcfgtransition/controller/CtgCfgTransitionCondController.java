package ngvgroup.com.rpt.features.ctgcfgtransition.controller;

import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgtransition.service.CtgCfgTransitionCondService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transition-cond")
public class CtgCfgTransitionCondController {
    private final CtgCfgTransitionCondService service;

    @LogActivity(function = "Xuất danh sách điều kiện")
    @Operation(summary = "Xuất danh sách điều kiện")
    @PostMapping("/export-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(
            @RequestParam List<String> labels,
            @RequestParam String keyword,
            @RequestParam String fileName,
            @RequestParam String transitionCode) {
        return service.exportToExcel(labels, keyword, fileName, transitionCode);
    }
}
