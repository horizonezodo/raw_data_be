package ngvgroup.com.bpmn.controller;

import ngvgroup.com.bpmn.dto.CtgCfgReport.*;
import ngvgroup.com.bpmn.dto.CtgCfgReportParam.ReportParamDto;
import ngvgroup.com.bpmn.dto.response.PageResponse;
import ngvgroup.com.bpmn.dto.response.ResponseData;
import ngvgroup.com.bpmn.service.CtgCfgReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/report")
public class CtgCfgReportController {

    private final CtgCfgReportService ctgCfgReportService;

    @GetMapping("/menu")
    public ResponseEntity<ResponseData<List<ReportMenu>>> getMenu() {
        List<ReportMenu> reportMenus = ctgCfgReportService.getMenu();
        return ResponseData.okEntity(reportMenus);
    }

    @GetMapping("/mining-menu")
    public ResponseEntity<ResponseData<List<ReportMiningMenu>>> getMiningMenu() {
        List<ReportMiningMenu> reportMenus = ctgCfgReportService.getMiningMenu();
        return ResponseData.okEntity(reportMenus);
    }

    @Operation(summary = "Lấy danh sách báo cáo theo group code + reportType", description = "Trả về danh sách báo cáo theo group code + reportType, hỗ trợ tìm kiếm theo từ khóa và phân trang.\n"
            + "- reportType: Loại báo cáo" +
            "- groupCode: Mã nhóm báo cáo" +
            "- keyword: Từ khóa tìm kiếm theo tên/miêu tả\n" +
            "- pageable: thông tin phân trang (page, size, sort)")
    @PostMapping("/search-all")
    public ResponseEntity<ResponseData<PageResponse<ReportDto>>> getAll(@RequestBody(required = false) List<ReportSearchRequest> requests,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<ReportDto> page = ctgCfgReportService.searchListReportByRequest(requests, keyword, pageable);
        PageResponse<ReportDto> response = new PageResponse<>(page);
        return ResponseData.okEntity(response);
    }

    @Operation(summary = "Tạo mới report", description = "Tạo mới một report" +
            "- TemplateCode phải là duy nhất")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<CtgCfgReportDTO>> create(@RequestBody CtgCfgReportDTO ctgCfgReportDTO) {
        CtgCfgReportDTO newCtgCfgReportDTO = ctgCfgReportService.create(ctgCfgReportDTO);
        return ResponseData.okEntity(newCtgCfgReportDTO);
    }

    @Operation(summary = "Cập nhật report", description = "Cập nhật một report")
    @PutMapping("/update/{reportCode}")
    public ResponseEntity<ResponseData<CtgCfgReportDTO>> update(@PathVariable String reportCode,
            @RequestBody CtgCfgReportDTO ctgCfgReportDTO) {
        CtgCfgReportDTO newCtgCfgReportDTO = ctgCfgReportService.update(ctgCfgReportDTO);
        return ResponseData.okEntity(newCtgCfgReportDTO);
    }

    @Operation(summary = "Lấy thông tin report", description = "Cập nhật một report")
    @GetMapping("/get/{reportCode}")
    public ResponseEntity<ResponseData<CtgCfgReportDTO>> getReport(@PathVariable String reportCode) {
        CtgCfgReportDTO newCtgCfgReportDTO = ctgCfgReportService.getReport(reportCode);
        return ResponseData.okEntity(newCtgCfgReportDTO);
    }

    @Operation(summary = "Xóa report", description = "Xóa một report")
    @DeleteMapping("/delete/{reportCode}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String reportCode) {
        ctgCfgReportService.deleteByReportCode(reportCode);
        return ResponseData.noContentEntity();
    }

    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable String fileName, @RequestBody ReportExcelDTO req) {
        try {
            byte[] excelData = ctgCfgReportService.generateExcelFile(req, fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xlsx\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Export failed: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

    // @Operation(summary = "Xem báo cáo chi tiết - CORE", description = "Xem báo cáo chi tiết - CORE")
    // @PostMapping("/export-file")
    // public ResponseEntity<byte[]> exportFileReport(@RequestBody ReportJasperDTO reportJasperDTO) throws Exception {

    //     byte[] report = ctgCfgReportService.generateReport(reportJasperDTO);

    //     String contentType;
    //     switch (reportJasperDTO.getFormat().toLowerCase()) {
    //         case "pdf":
    //             contentType = "application/pdf";
    //             break;
    //         case "xlsx":
    //             contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    //             break;
    //         case "docx":
    //             contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    //             break;
    //         default:
    //             return ResponseEntity.badRequest().build();
    //     }

    //     return ResponseEntity.ok()
    //             .header("Content-Type", contentType)
    //             .header("Content-Disposition", "attachment; filename=report." + reportJasperDTO.getFormat())
    //             .body(report);
    // }

    @Operation(summary = "Xem báo cáo chi tiết - CORE", description = "Xem báo cáo chi tiết - CORE")
    @PostMapping("/export")
    public ResponseEntity<ResponseData<CtgCfgReportDTO>> exportUrlReport(@RequestBody ReportParamDto reportParamDto) {
        CtgCfgReportDTO ctgCfgReportDTO = ctgCfgReportService.exportUrlReport(reportParamDto);
        return ResponseData.okEntity(ctgCfgReportDTO);
    }

    @Operation(summary = "Xóa nhiều report", description = "Xóa nhiều report theo danh sách id")
    @DeleteMapping("/delete-all")
    public ResponseEntity<ResponseData<Void>> deleteAll(@RequestBody List<Long> ids) {
        ctgCfgReportService.deleteByIds(ids);
        return ResponseData.noContentEntity();
    }

    @Operation(summary = "Lấy danh sách cài đặt báo cáo", description = "Lấy danh sách báo cáo có reportType = 'CM036.001' với phân trang")
    @GetMapping("/setting")
    public ResponseEntity<ResponseData<PageResponse<ReportDto>>> getListReportSetting(
            @RequestParam(required = false, defaultValue = "CM036.001") String reportType,
            Pageable pageable) {
        Page<ReportDto> page = ctgCfgReportService.getListReportSetting(reportType, pageable);
        PageResponse<ReportDto> response = new PageResponse<>(page);
        return ResponseData.okEntity(response);
    }


    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String reportCode) {
        return ResponseData.okEntity(ctgCfgReportService.checkExist(reportCode));
    }
}
