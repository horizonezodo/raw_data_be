package ngvgroup.com.bpmn.controller;

import ngvgroup.com.bpmn.dto.CtgCfgReportParamCond.CtgCfgReportParamCondDTO;
import ngvgroup.com.bpmn.dto.CtgCfgReportParamCond.ReportParamCondDTO;
import ngvgroup.com.bpmn.dto.response.PageResponse;
import ngvgroup.com.bpmn.dto.response.ResponseData;
import ngvgroup.com.bpmn.service.CtgCfgReportParamCondService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/report-param-cond")
public class CtgCfgReportParamCondController {
    private final CtgCfgReportParamCondService ctgCfgReportParamCondService;

    @Operation(summary = "Lấy danh sách ràng buộc tham số", description = "Trả về danh sách ràng buộc tham số, hỗ trợ tìm kiếm theo từ khóa và phân trang.\n"
            +
            "- keyword: Từ khóa tìm kiếm theo tên/miêu tả\n" +
            "- pageable: thông tin phân trang (page, size, sort)")
    @GetMapping("/search-all")
    public ResponseEntity<ResponseData<PageResponse<ReportParamCondDTO>>> searchAll(
                                                                             @RequestParam(required = false) String reportCode,
                                                                             @RequestParam(required = false) String keyword,
                                                                             Pageable pageable) {
        Page<ReportParamCondDTO> page = ctgCfgReportParamCondService.searchAll(reportCode, keyword, pageable);
        PageResponse<ReportParamCondDTO> response = new PageResponse<>(page);
        return ResponseData.okEntity(response);
    }

//    @Operation(summary = "Lấy danh sách ràng buộc tham số theo report code")
//    @GetMapping("/get-all/{reportCode}")
//    public ResponseEntity<ResponseData<List<ReportParamCondDTO>>> getAll(@PathVariable String reportCode) {
//        List<ReportParamCondDTO> reportParamCondDTOS = ctgCfgReportParamCondService.getAllByReportCode(reportCode);
//        return ResponseData.okEntity(reportParamCondDTOS);
//    }

    @Operation(summary = "Tạo mới ràng buộc tham số", description = "Tạo mới một ràng buộc tham số")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<CtgCfgReportParamCondDTO>> create(@RequestBody CtgCfgReportParamCondDTO ctgCfgReportParamCondDTO) {
        CtgCfgReportParamCondDTO ctgCfgReportParamCondDTO1 = ctgCfgReportParamCondService.create(ctgCfgReportParamCondDTO);
        return ResponseData.okEntity(ctgCfgReportParamCondDTO1);
    }

    @Operation(summary = "Cập nhật tham số đầu vào", description = "Cập nhật một tham số đầu vào")
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseData<CtgCfgReportParamCondDTO>> update(@PathVariable Long id,
                                                                         @RequestBody CtgCfgReportParamCondDTO ctgCfgReportParamCondDTO) {
        CtgCfgReportParamCondDTO newCfgReportParamDTO =  ctgCfgReportParamCondService.update(ctgCfgReportParamCondDTO);
        return ResponseData.okEntity(newCfgReportParamDTO);
    }

    @Operation(summary = "Xóa ràng buôc tham số", description = "Xóa một ràng buộc tham số")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable Long id) {
        ctgCfgReportParamCondService.deleteById(id);
        return ResponseData.noContentEntity();
    }

    @Operation(summary = "Lấy thông tin 1 ràng buôc tham số", description = "Lấy thông tin 1 ràng buôc tham số")
    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseData<CtgCfgReportParamCondDTO>> get(@PathVariable Long id) {
        CtgCfgReportParamCondDTO ctgCfgReportParamCondDTO = ctgCfgReportParamCondService.findById(id);
        return ResponseData.okEntity(ctgCfgReportParamCondDTO);
    }
}
