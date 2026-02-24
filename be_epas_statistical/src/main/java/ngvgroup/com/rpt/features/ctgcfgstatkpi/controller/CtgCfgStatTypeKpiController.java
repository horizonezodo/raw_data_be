package ngvgroup.com.rpt.features.ctgcfgstatkpi.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatTypeKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.service.CtgCfgStatTypeKpiService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stat-type-kpi")
@AllArgsConstructor
public class CtgCfgStatTypeKpiController {
    private final CtgCfgStatTypeKpiService ctgCfgStatTypeKpiService;

    @LogActivity(function = "Lấy danh sách loại chỉ tiêu")
    @Operation(summary = "Lấy danh sách loại chỉ tiêu")
    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<CtgCfgStatTypeKpiDto>>> getAll() {

        return ResponseData.okEntity(ctgCfgStatTypeKpiService.getAll());
    }

    @LogActivity(function = "Phân trang loại chỉ tiêu")
    @GetMapping
    public ResponseEntity<ResponseData<Page<CtgCfgStatTypeKpiDto>>> pageTypeKpi(@RequestParam("keyword")String keyword,
                                                                                @ParameterObject Pageable pageable){
        return ResponseData.okEntity(ctgCfgStatTypeKpiService.pageTypeKpi(keyword, pageable));
    }
    @LogActivity(function = "Lấy chi tiết loại chỉ tiêu")
    @Operation(summary = "Chỉ tiết loại chỉ tiêu")
    @GetMapping("/get/{kpiTypeCode}")
    public ResponseEntity<ResponseData<CtgCfgStatTypeKpiDto>> getOneKpiType(@PathVariable("kpiTypeCode")String kpiTypeCode){
        return ResponseData.okEntity(ctgCfgStatTypeKpiService.getOne(kpiTypeCode));
    }
    @LogActivity(function = "Khởi tạo loại chỉ tiêu")
    @Operation(summary = "Khởi tạo loại chỉ tiêu")
    @PostMapping("/create")
    public ResponseEntity<ResponseData<Void>> createKpiType(@RequestBody CtgCfgStatTypeKpiDto dto){
        ctgCfgStatTypeKpiService.createTypeKpi(dto);
        return ResponseData.createdEntity();
    }
    @LogActivity(function = "Cập nhật loại chỉ tiêu")
    @Operation(summary = "Update loại chỉ tiêu")
    @PostMapping("/update/{kpiTypeCode}")
    public ResponseEntity<ResponseData<Void>> updateKpiType(@PathVariable("kpiTypeCode")String kpiTypeCode,
                                                            @RequestBody CtgCfgStatTypeKpiDto dto){
        ctgCfgStatTypeKpiService.updateTypeKpi(dto,kpiTypeCode);
        return ResponseData.noContentEntity();
    }
    @LogActivity(function = "Xóa loại chỉ tiêu")
    @Operation(summary = "Xóa loại chỉ tiêu")
    @PostMapping("/delete/{kpiTypeCode}")
    public ResponseEntity<ResponseData<Void>> deleteKpiType(@PathVariable("kpiTypeCode")String kpiTypeCode){
        ctgCfgStatTypeKpiService.deleteTypeKpi(kpiTypeCode);
        return ResponseData.noContentEntity();
    }

    @LogActivity(function = "Xuất danh sách chỉ tiêu")
    @Operation(summary = "Xuất danh sách chỉ tiêu")
    @PostMapping("/export-to-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(@RequestParam List<String> labels,
                                                         @RequestParam String keyword,
                                                         @RequestParam String fileName){
        return ctgCfgStatTypeKpiService.exportToExcel(labels,keyword,fileName);
    }

    @LogActivity(function = "Kiểm tra loại chỉ tiêu tồn tại")
    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String kpiTypeCode) {
        return ResponseData.okEntity(ctgCfgStatTypeKpiService.checkExistStatScoreGroupCode(kpiTypeCode));
    }
}
