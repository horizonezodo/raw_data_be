package ngvgroup.com.rpt.features.report.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreport.*;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.ReportParamDto;
import ngvgroup.com.rpt.features.report.helper.PageResponse;
import ngvgroup.com.rpt.features.report.service.CtgCfgReportService;
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

    @LogActivity(function = "Lấy menu báo cáo")
    @GetMapping("/menu")
    public ResponseEntity<ResponseData<List<ReportMenu>>> getMenu() {
        List<ReportMenu> reportMenus = ctgCfgReportService.getMenu();
        return ResponseData.okEntity(reportMenus);
    }

    @LogActivity(function = "Lấy menu khai thác")
    @GetMapping("/mining-menu")
    public ResponseEntity<ResponseData<List<ReportMiningMenu>>> getMiningMenu() {
        List<ReportMiningMenu> reportMenus = ctgCfgReportService.getMiningMenu();
        return ResponseData.okEntity(reportMenus);
    }

    @LogActivity(function = "Tìm kiếm báo cáo")
    @Operation(
            summary = "Lấy danh sách báo cáo theo group code + reportType",
            description = """
                    Trả về danh sách báo cáo theo group code + reportType, hỗ trợ tìm kiếm theo từ khóa và phân trang.
                    - reportType: Loại báo cáo
                    - groupCode: Mã nhóm báo cáo
                    - keyword: Từ khóa tìm kiếm theo tên/miêu tả
                    - pageable: thông tin phân trang (page, size, sort)
                    """
    )
    @PostMapping("/search-all")
    public ResponseEntity<ResponseData<PageResponse<ReportDtoV1>>> getAll(@RequestBody(required = false) List<ReportSearchRequest> requests,
                                                                        @RequestParam(required = false) String keyword,
                                                                        Pageable pageable) {
        Page<ReportDtoV1> page = ctgCfgReportService.searchListReportByRequest(requests, keyword, pageable);
        PageResponse<ReportDtoV1> response = new PageResponse<>(page);
        return ResponseData.okEntity(response);
    }

    @LogActivity(function = "Tạo mới report")
    @Operation(summary = "Tạo mới report", description = "Tạo mới một report" +
            "- TemplateCode phải là duy nhất")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<CtgCfgReportDTO>> create(@RequestBody CtgCfgReportDTO ctgCfgReportDTO) {
        CtgCfgReportDTO newCtgCfgReportDTO = ctgCfgReportService.create(ctgCfgReportDTO);
        return ResponseData.okEntity(newCtgCfgReportDTO);
    }

    @LogActivity(function = "Cập nhật report")
    @Operation(summary = "Cập nhật report", description = "Cập nhật một report")
    @PutMapping("/update/{reportCode}")
    public ResponseEntity<ResponseData<CtgCfgReportDTO>> update(@PathVariable String reportCode,
                                                                @RequestBody CtgCfgReportDTO ctgCfgReportDTO) {
        CtgCfgReportDTO newCtgCfgReportDTO = ctgCfgReportService.update(ctgCfgReportDTO);
        return ResponseData.okEntity(newCtgCfgReportDTO);
    }

    @LogActivity(function = "Lấy thông tin report")
    @Operation(summary = "Lấy thông tin report", description = "Cập nhật một report")
    @GetMapping("/get/{reportCode}")
    public ResponseEntity<ResponseData<CtgCfgReportDTO>> getReport(@PathVariable String reportCode) {
        CtgCfgReportDTO newCtgCfgReportDTO = ctgCfgReportService.getReport(reportCode);
        return ResponseData.okEntity(newCtgCfgReportDTO);
    }

    @LogActivity(function = "Xóa report")
    @Operation(summary = "Xóa report", description = "Xóa một report")
    @DeleteMapping("/delete/{reportCode}")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String reportCode) {
        ctgCfgReportService.deleteByReportCode(reportCode);
        return ResponseData.noContentEntity();
    }

    @LogActivity(function = "Xuất Excel báo cáo")
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable String fileName, @RequestBody ReportExcelDTO req) {
        try {
            byte[] excelData = ctgCfgReportService.generateExcelFile(req, fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xlsx\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Export failed: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

    @LogActivity(function = "Xem báo cáo chi tiết")
    @Operation(summary = "Xem báo cáo chi tiết - CORE", description = "Xem báo cáo chi tiết - CORE")
    @PostMapping("/export")
    public ResponseEntity<ResponseData<CtgCfgReportDTO>> exportUrlReport(@RequestBody ReportParamDto reportParamDto) {
        CtgCfgReportDTO ctgCfgReportDTO = ctgCfgReportService.exportUrlReport(reportParamDto);
        return ResponseData.okEntity(ctgCfgReportDTO);
    }

    @LogActivity(function = "Xóa nhiều report")
    @Operation(summary = "Xóa nhiều report", description = "Xóa nhiều report theo danh sách id")
    @DeleteMapping("/delete-all")
    public ResponseEntity<ResponseData<Void>> deleteAll(@RequestBody List<Long> ids) {
        ctgCfgReportService.deleteByIds(ids);
        return ResponseData.noContentEntity();
    }

    @LogActivity(function = "Lấy danh sách cài đặt báo cáo")
    @Operation(summary = "Lấy danh sách cài đặt báo cáo", description = "Lấy danh sách báo cáo có reportType = 'CM036.001' với phân trang")
    @GetMapping("/setting")
    public ResponseEntity<ResponseData<PageResponse<ReportDto>>> getListReportSetting(
            @RequestParam(required = false, defaultValue = "CM036.001") String reportType,
            Pageable pageable) {
        Page<ReportDto> page = ctgCfgReportService.getListReportSetting(reportType, pageable);
        PageResponse<ReportDto> response = new PageResponse<>(page);
        return ResponseData.okEntity(response);
    }

    @LogActivity(function = "Kiểm tra report tồn tại")
    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String reportCode) {
        return ResponseData.okEntity(ctgCfgReportService.checkExist(reportCode));
    }
}
