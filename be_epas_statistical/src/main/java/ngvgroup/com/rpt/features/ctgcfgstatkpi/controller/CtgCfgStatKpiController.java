package ngvgroup.com.rpt.features.ctgcfgstatkpi.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.service.CtgCfgStatKpiService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stat-kpi")
@AllArgsConstructor
public class CtgCfgStatKpiController {
    private final CtgCfgStatKpiService ctgCfgStatKpiService;

    @LogActivity(function = "Tìm kiếm tất cả KPI thống kê")
    @PostMapping("/search-all")
    public ResponseEntity<ResponseData<Page<CtgCfgStatKpiDto>>> getAll( @RequestParam String keyword, @RequestParam(required = false) List<String> kpiTypeCodes, @ParameterObject Pageable pageable) {

        return ResponseData.okEntity(ctgCfgStatKpiService.searchAllStatKpi(keyword,kpiTypeCodes,pageable));
    }

    @LogActivity(function = "Xuất Excel danh sách KPI")
    @PostMapping("/export-to-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(@RequestParam List<String> labels, @RequestParam(required = false) List<String> kpiTypeCodes,@RequestParam String fileName){
        return ctgCfgStatKpiService.exportToExcel(labels,kpiTypeCodes,fileName);
    }

    @LogActivity(function = "Lấy tất cả dữ liệu KPI")
    @GetMapping()
    public ResponseEntity<ResponseData<Page<CtgCfgStatKpiDto>>> findAllKpiData(@RequestParam String keyword, @ParameterObject Pageable pageable){
        return ResponseData.okEntity(this.ctgCfgStatKpiService.getAllKpiData(keyword, pageable));
    }


    @LogActivity(function = "Tạo mới KPI thống kê")
    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgCfgStatKpiDto ctgCfgStatKpiDto) {
        ctgCfgStatKpiService.create(ctgCfgStatKpiDto);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Cập nhật KPI thống kê")
    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@RequestBody CtgCfgStatKpiDto ctgCfgStatKpiDto) {
        ctgCfgStatKpiService.update(ctgCfgStatKpiDto);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Lấy chi tiết KPI thống kê")
    @GetMapping("/get-detail")
    public ResponseEntity<ResponseData<CtgCfgStatKpiDto>> getDetail(@RequestParam Long id) {

        return ResponseData.okEntity(ctgCfgStatKpiService.getDetail(id));
    }

    @LogActivity(function = "Xóa KPI thống kê")
    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam Long id) {
        ctgCfgStatKpiService.delete(id);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Kiểm tra KPI tồn tại")
    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String kpiCode) {
        return ResponseData.okEntity(ctgCfgStatKpiService.checkExist(kpiCode));
    }

    @LogActivity(function = "Lấy KPI theo mã")
    @GetMapping("/get-by-kpi-code")
    public ResponseEntity<ResponseData<CtgCfgStatKpiDto>> getByKpiCode(@RequestParam String kpiCode) {
        return ResponseData.okEntity(ctgCfgStatKpiService.getByKpiCode(kpiCode));
    }



}
