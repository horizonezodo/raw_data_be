package ngvgroup.com.rpt.features.smrtxnscore.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.smrscore.dto.BranchResultDto;
import ngvgroup.com.rpt.features.smrtxnscore.dto.ScoreCalculationRequestDto;
import ngvgroup.com.rpt.features.smrtxnscore.dto.ScoreSaveRequest;
import ngvgroup.com.rpt.features.smrtxnscore.service.ScoreCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/score")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreCalculationService service;
    private final ExportExcel exportExcel;

    @LogActivity(function = "Tính toán điểm")
    @PostMapping("/calculate")
    public ResponseEntity<ResponseData<List<BranchResultDto>>> calculate(
            @RequestParam(required = false) List<String> keywords,
            @RequestBody ScoreCalculationRequestDto req) {
        return ResponseData.okEntity(service.calculatePreview(keywords, req));
    }

    @LogActivity(function = "Lưu điểm")
    @PostMapping("/save")
    public ResponseEntity<ResponseData<String>> save(@RequestBody ScoreSaveRequest saveReq) {
        service.saveScore(saveReq.getReq(), saveReq.getBranchResults(), saveReq.getMakerUserCode(), saveReq.getMakerUserName());
        return ResponseData.okEntity("Success");
    }

    @LogActivity(function = "Tạo mã chấm điểm")
    @GetMapping("/generate-score-instance-code")
    public ResponseEntity<ResponseData<String>> generateScoreInstanceCode() {
           return ResponseData.okEntity(service.generateScoreInstanceCode());
    }

    @LogActivity(function = "Xuất Excel tính điểm")
    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam(required = false) List<String> keywords,
            @RequestBody ScoreCalculationRequestDto req
    ) throws Exception {
        return exportExcel.exportExcel(service.calculatePreview(keywords, req), "Thông_tin_danh_sách_chi_nhánh.xlsx");
    }
}
