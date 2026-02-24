package ngvgroup.com.rpt.features.ctgcfgai.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTO;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTOV1;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTOV2;
import ngvgroup.com.rpt.features.ctgcfgai.service.CtgCfgAiToolService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-tool")
public class CtgCfgAiToolController {
    private final CtgCfgAiToolService service;

    @LogActivity(function = "Lấy danh sách công cụ AI")
    @Operation(summary = "Lấy danh sách công cụ AI")
    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgAiToolDTOV1>>> pageAiTool(@RequestParam String keyword,
                                                                            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(service.pageAiTool(keyword, pageable));
    }

    @LogActivity(function = "Lấy tất cả công cụ AI")
    @Operation(summary = "Lấy tất cả công cụ AI")
    @GetMapping("/all")
    public ResponseEntity<ResponseData<List<CtgCfgAiToolDTOV2>>> getAllTools() {
        return ResponseData.okEntity(service.getAllTools());
    }

    @LogActivity(function = "Lấy thông tin chi tiết công cụ AI")
    @Operation(summary = "Lấy thông tin chi tiết công cụ AI")
    @GetMapping("/get/{toolAiCode}")
    public ResponseEntity<ResponseData<CtgCfgAiToolDTO>> getDetail(@PathVariable("toolAiCode") String toolAiCode) {
        return ResponseData.okEntity(service.getDetail(toolAiCode));
    }

    @LogActivity(function = "Thêm công cụ AI")
    @Operation(summary = "Thêm công cụ AI")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createAITool(@RequestBody CtgCfgAiToolDTO dto) {
        service.createAiTool(dto);
        return ResponseData.createdEntity();
    }

    @LogActivity(function = "Sửa công cụ AI")
    @Operation(summary = "Sửa công cụ AI")
    @PostMapping("/update/{toolAiCode}")
    public ResponseEntity<ResponseData<Void>> updateAITool(@RequestBody CtgCfgAiToolDTO dto, @PathVariable("toolAiCode") String toolAiCode) {
        service.updateAiTool(dto, toolAiCode);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Xóa công cụ AI")
    @Operation(summary = "Xóa công cụ AI")
    @PostMapping("/delete/{toolAiCode}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable("toolAiCode") String toolAiCode) {
        service.deleteAiTool(toolAiCode);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Xuất danh sách công cụ AI")
    @Operation(summary = "Xuất danh sách công cụ AI")
    @PostMapping("/export-to-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(@RequestParam List<String> labels, @RequestParam String keyword, @RequestParam String fileName) {
        return service.exportToExcel(labels, keyword, fileName);
    }
}
