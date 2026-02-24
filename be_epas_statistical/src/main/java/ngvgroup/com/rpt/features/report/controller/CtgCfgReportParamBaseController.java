package ngvgroup.com.rpt.features.report.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import ngvgroup.com.rpt.features.report.dto.ExportExcelRequest;
import ngvgroup.com.rpt.features.report.dto.SearchFilterRequest;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparambase.CtgCfgReportParamBaseDto;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparambase.CtgCfgReportParamBaseResponse;
import ngvgroup.com.rpt.features.report.service.CtgCfgReportParamBaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com-cfg-report-param-base")
public class CtgCfgReportParamBaseController {
    private final CtgCfgReportParamBaseService ctgCfgReportParamBaseService;

    public CtgCfgReportParamBaseController(CtgCfgReportParamBaseService ctgCfgReportParamBaseService) {
        this.ctgCfgReportParamBaseService = ctgCfgReportParamBaseService;
    }

    @LogActivity(function = "Tạo cấu hình tham số báo cáo")
    @PostMapping
    public ResponseEntity<ResponseData<Void>> createComCfgReportParamBase(
            @RequestBody CtgCfgReportParamBaseDto comCfgReportParamBase) {
        ctgCfgReportParamBaseService.createCfgReportParamBase(comCfgReportParamBase);
        return ResponseData.createdEntity();
    }

    @LogActivity(function = "Cập nhật cấu hình tham số báo cáo")
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseData<Void>> updateComCfgReportParamBase(
            @PathVariable("id") long id,
            @RequestBody CtgCfgReportParamBaseDto comCfgReportParamBase
    ) {
        ctgCfgReportParamBaseService.updateCfgReportParamBase(id, comCfgReportParamBase);
        return ResponseData.noContentEntity();
    }

    @LogActivity(function = "Lấy chi tiết cấu hình tham số báo cáo")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<CtgCfgReportParamBaseDto>> getDetailComCfgReportParamBase(@PathVariable("id") long id) {
        return ResponseData.okEntity(ctgCfgReportParamBaseService.getDetailCfgReportParamBase(id));
    }

    @LogActivity(function = "Xóa cấu hình tham số báo cáo")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseData<Void>> deleteComCfgReportParamBase(@PathVariable("id") long id) {
        ctgCfgReportParamBaseService.deleteCfgReportParamBase(id);
        return ResponseData.noContentEntity();
    }

    @LogActivity(function = "Tìm kiếm cấu hình tham số báo cáo")
    @PostMapping("/search")
    public ResponseEntity<ResponseData<List<CtgCfgReportParamBaseResponse>>> searchComCfgReportParamBase(
            @RequestBody SearchFilterRequest request) {

        return ResponseData.okEntity(ctgCfgReportParamBaseService.searchCfgReportParamBasev2(request));
    }

    @LogActivity(function = "Xuất Excel cấu hình tham số báo cáo")
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> exportComCfgMailTemplate(
            @PathVariable("fileName") String fileName,
            @RequestBody ExportExcelRequest request) {
        return ctgCfgReportParamBaseService.exportExcel(fileName, request);
    }

    @LogActivity(function = "Lấy tất cả cấu hình tham số")
    @GetMapping("/get-all")
    public ResponseEntity<ResponseData<List<CtgCfgReportParamBaseResponse>>> getAllComCfgReportParamBase() {
        return ResponseData.okEntity(ctgCfgReportParamBaseService.getAllCfgReportParamBase());
    }

    @LogActivity(function = "Kiểm tra cấu hình tham số tồn tại")
    @GetMapping("/check-exist")
    public ResponseEntity<ResponseData<Boolean>> checkExistComCfgReportParamBase(@RequestParam String paramBaseCode) {
        return ResponseData.okEntity(ctgCfgReportParamBaseService.isExistedByParamBaseCode(paramBaseCode));
    }

}
