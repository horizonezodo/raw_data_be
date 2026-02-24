package ngvgroup.com.rpt.features.ctgcfgstattemplate.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.CtgCfgStatTypeListDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.CtgCfgStatTypeResponse;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.StatFilterTreeDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatType;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/stat-type")
@RequiredArgsConstructor
@Tag(name = "CtgCfgStatType", description = "Loại thống kê")
@RestController
public class CtgCfgStatTypeController {

    private final CtgCfgStatTypeService service;

    @LogActivity(function = "Tìm kiếm loại thống kê")
    @PostMapping("/search")
    @Operation(summary = "Tìm kiếm loại thống kê")
    public ResponseEntity<ResponseData<Page<CtgCfgStatTypeListDto>>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> reportModuleCodes,
            Pageable pageable) {
        return ResponseData.okEntity(service.search(keyword, reportModuleCodes, pageable));
    }

    @LogActivity(function = "Lấy chi tiết loại thống kê")
    @GetMapping("/detail/{code}")
    @Operation(summary = "Chi tiết loại thống kê")
    public ResponseEntity<ResponseData<CtgCfgStatType>> detail(@PathVariable("code") String code) {
        return ResponseData.okEntity(service.detail(code));
    }

    @LogActivity(function = "Tạo mới loại thống kê")
    @PostMapping("/create")
    @Operation(summary = "Tạo mới loại thống kê")
    public ResponseEntity<ResponseData<CtgCfgStatType>> create(@RequestBody CtgCfgStatType payload) {
        return ResponseData.okEntity(service.create(payload));
    }

    @LogActivity(function = "Cập nhật loại thống kê")
    @PutMapping("/update/{code}")
    @Operation(summary = "Cập nhật loại thống kê")
    public ResponseEntity<ResponseData<CtgCfgStatType>> update(@PathVariable("code") String code,
                                                               @RequestBody CtgCfgStatType payload) {
        return ResponseData.okEntity(service.update(code, payload));
    }

    @LogActivity(function = "Xóa loại thống kê")
    @DeleteMapping("/delete/{code}")
    @Operation(summary = "Xóa loại thống kê")
    public ResponseEntity<ResponseData<Boolean>> delete(@PathVariable("code") String code) {
        service.delete(code);
        return ResponseData.okEntity(true);
    }

    @LogActivity(function = "Xuất Excel loại thống kê")
    @PostMapping("/download-excel")
    @Operation(summary = "Xuất Excel loại thống kê")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam String keyword,
            @RequestParam(required = false) List<String> reportModuleCodes,
            @RequestParam String fileName,
            Pageable pageable) {
        return service.exportToExcel(reportModuleCodes, fileName, keyword, pageable);
    }

    @LogActivity(function = "Kiểm tra mã loại thống kê tồn tại")
    @GetMapping("/exists/{code}")
    @Operation(summary = "Kiểm tra mã loại TK đã tồn tại")
    public ResponseEntity<ResponseData<Boolean>> exists(@PathVariable("code") String code) {
        return ResponseData.okEntity(service.existsByCode(code));
    }

    @LogActivity(function = "Lấy loại thống kê theo mã module")
    @GetMapping("get-stat-tye-by-report-module-code")
    public ResponseEntity<ResponseData<List<CtgCfgStatTypeResponse>>> getStatTypeByReportModuleCode(@RequestParam String commonCode) {
        return ResponseData.okEntity(service.getStatTypeByReportModuleCode(commonCode));
    }

    @LogActivity(function = "Lấy tất cả loại thống kê")
    @GetMapping("getAll")
    public ResponseEntity<ResponseData<List<CtgCfgStatTypeResponse>>> getAllStatType(@RequestParam(defaultValue = "") String keyword) {
        return ResponseData.okEntity(service.getAllStatType(keyword));
    }

    @LogActivity(function = "Lấy cây lọc thống kê")
    @GetMapping("filter-tree")
    public List<StatFilterTreeDto> getStatFilterTree() {
        return service.getStatFilterTree();
    }
}
