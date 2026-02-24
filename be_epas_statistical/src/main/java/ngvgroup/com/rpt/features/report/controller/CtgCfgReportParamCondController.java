package ngvgroup.com.rpt.features.report.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparamcond.CtgCfgReportParamCondDTO;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparamcond.ReportParamCondDTO;
import ngvgroup.com.rpt.features.report.helper.PageResponse;
import ngvgroup.com.rpt.features.report.service.CtgCfgReportParamCondService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/report-param-cond")
public class CtgCfgReportParamCondController {
    private final CtgCfgReportParamCondService ctgCfgReportParamCondService;

    @Operation(
            summary = "Lấy danh sách ràng buộc tham số",
            description = """
            Trả về danh sách ràng buộc tham số, hỗ trợ tìm kiếm theo từ khóa và phân trang.
            - keyword: Từ khóa tìm kiếm theo tên/miêu tả
            - pageable: thông tin phân trang (page, size, sort)
            """
    )
    @LogActivity(function = "Lấy danh sách ràng buộc tham số")
    @GetMapping("/search-all")
    public ResponseEntity<ResponseData<PageResponse<ReportParamCondDTO>>> searchAll(
            @RequestParam(required = false) String reportCode,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<ReportParamCondDTO> page = ctgCfgReportParamCondService.searchAll(reportCode, keyword, pageable);
        PageResponse<ReportParamCondDTO> response = new PageResponse<>(page);
        return ResponseData.okEntity(response);
    }

    @LogActivity(function = "Tạo ràng buộc tham số")
    @Operation(summary = "Tạo mới ràng buộc tham số", description = "Tạo mới một ràng buộc tham số")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<CtgCfgReportParamCondDTO>> create(@RequestBody CtgCfgReportParamCondDTO ctgCfgReportParamCondDTO) {
        CtgCfgReportParamCondDTO ctgCfgReportParamCondDTO1 = ctgCfgReportParamCondService.create(ctgCfgReportParamCondDTO);
        return ResponseData.okEntity(ctgCfgReportParamCondDTO1);
    }

    @LogActivity(function = "Cập nhật tham số đầu vào")
    @Operation(summary = "Cập nhật tham số đầu vào", description = "Cập nhật một tham số đầu vào")
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseData<CtgCfgReportParamCondDTO>> update(@PathVariable Long id,
                                                                         @RequestBody CtgCfgReportParamCondDTO ctgCfgReportParamCondDTO) {
        CtgCfgReportParamCondDTO newCfgReportParamDTO = ctgCfgReportParamCondService.update(ctgCfgReportParamCondDTO);
        return ResponseData.okEntity(newCfgReportParamDTO);
    }

    @LogActivity(function = "Xóa ràng buộc tham số")
    @Operation(summary = "Xóa ràng buôc tham số", description = "Xóa một ràng buộc tham số")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable Long id) {
        ctgCfgReportParamCondService.deleteById(id);
        return ResponseData.noContentEntity();
    }

    @LogActivity(function = "Lấy thông tin ràng buộc tham số")
    @Operation(summary = "Lấy thông tin 1 ràng buôc tham số", description = "Lấy thông tin 1 ràng buôc tham số")
    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseData<CtgCfgReportParamCondDTO>> get(@PathVariable Long id) {
        CtgCfgReportParamCondDTO ctgCfgReportParamCondDTO = ctgCfgReportParamCondService.findById(id);
        return ResponseData.okEntity(ctgCfgReportParamCondDTO);
    }
}
