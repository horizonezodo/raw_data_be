package ngvgroup.com.rpt.features.ctgcfgstatscorerank.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreBenchmarkDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreBenchmarkRequest;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreBenchmark;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.service.CtgCfgStatScoreBenchmarkService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/score-benchmark")
@AllArgsConstructor
public class CtgCfgStatCoreBenchmarkController {
    private CtgCfgStatScoreBenchmarkService service;

    @LogActivity(function = "Lấy tất cả điểm chuẩn")
    @GetMapping("get-all")
    public ResponseEntity<ResponseData<Page<StatScoreBenchmarkDto>>> getAll(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseData.okEntity(service.getAll(keyword, pageable));
    }

    @LogActivity(function = "Thêm điểm chuẩn")
    @PostMapping("add")
    public ResponseEntity<ResponseData<CtgCfgStatScoreBenchmark>> add(@RequestBody StatScoreBenchmarkRequest request) {
        return ResponseData.okEntity(service.add(request));
    }

    @LogActivity(function = "Cập nhật điểm chuẩn")
    @PutMapping("update")
    public ResponseEntity<ResponseData<CtgCfgStatScoreBenchmark>> update(@RequestBody StatScoreBenchmarkRequest request) {
        return ResponseData.okEntity(service.edit(request));
    }

    @LogActivity(function = "Xóa điểm chuẩn")
    @DeleteMapping("delete")
    public ResponseEntity<ResponseData<String>> delete(@RequestParam String benchmarkCode) {
        service.delete(benchmarkCode);
        return ResponseData.okEntity("delete success");
    }

    @LogActivity(function = "Lấy chi tiết điểm chuẩn")
    @GetMapping("detail")
    public ResponseEntity<ResponseData<StatScoreBenchmarkDto>> detail(@RequestParam String statScoreGroupCode) {
        return ResponseData.okEntity(service.getDetail(statScoreGroupCode));
    }

    @LogActivity(function = "Xuất Excel điểm chuẩn")
    @PostMapping("/download-excel")
    public ResponseEntity<ByteArrayResource> exportExcel(
            @RequestParam java.util.List<String> labels,
            @RequestParam(required = false) List<String> benchmarkCode,
            @RequestParam String fileName) {
        return service.exportToExcel(labels, benchmarkCode, fileName);
    }

    @LogActivity(function = "Tìm điểm chuẩn theo mã")
    @GetMapping("/find-by-code")
    public ResponseEntity<ResponseData<List<CtgCfgStatScoreBenchmark>>> findByCode(@RequestParam String statScoreTypeCode){
        return ResponseData.okEntity(service.getByCode(statScoreTypeCode));
    }
}
