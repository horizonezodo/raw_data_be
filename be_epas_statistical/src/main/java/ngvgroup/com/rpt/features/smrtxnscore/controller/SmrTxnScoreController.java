package ngvgroup.com.rpt.features.smrtxnscore.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.smrscore.dto.BranchResultDto;
import ngvgroup.com.rpt.features.smrscore.dto.BranchScoreCommonInfo;
import ngvgroup.com.rpt.features.smrtxnscore.dto.*;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import ngvgroup.com.rpt.features.smrtxnscore.service.SmrTxnScoreService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("rank-score")
public class SmrTxnScoreController {
    private final SmrTxnScoreService service;
    private final ExportExcel exportExcel;

    @LogActivity(function = "Tìm kiếm điểm xếp hạng")
    @PostMapping
    public ResponseEntity<ResponseData<Page<SmrTxnScorePageDTO>>> pageScore(@RequestParam String keyword,
                                                                            @RequestBody SearchFilterDTO dto,
                                                                            @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(service.pageScore(keyword, dto, pageable));
    }

    @LogActivity(function = "Xuất Excel điểm xếp hạng")
    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestParam String keyword,
                                              @RequestBody SearchFilterDTO dto) throws Exception {
        List<SmrTxnScoreExportExcelDTO> data = service.exportExcel(keyword, dto);
        return exportExcel.exportExcel(data, "smrTxnScore.xlsx");
    }

    @LogActivity(function = "Thay đổi trạng thái điểm")
    @PostMapping("change-status/{id}")
    public ResponseEntity<ResponseData<String>> changeStatus(@PathVariable Long id, @RequestBody ChangeStatusDto dto) {
        service.changeStatus(id, dto);
        return ResponseData.okEntity("Success");
    }

    @LogActivity(function = "Lấy bước tiếp theo")
    @GetMapping("next-step/{id}")
    public ResponseEntity<ResponseData<List<NextStepDto>>> changeStatus(@PathVariable Long id) {
        return ResponseData.okEntity(service.getNextSteps(id));
    }

    @LogActivity(function = "Lấy chi tiết điểm xếp hạng")
    @GetMapping("/{scoreInstanceCode}")
    public ResponseEntity<ResponseData<SmrTxnScoreDetailDTO>> getDetail(@PathVariable("scoreInstanceCode") String scoreInstanceCode) {
        return ResponseData.okEntity(service.getDetailScore(scoreInstanceCode));
    }

    @LogActivity(function = "Lấy chi nhánh theo điểm")
    @PostMapping("score-branch")
    public ResponseEntity<ResponseData<Page<BranchScoreCommonInfo>>> getBranch(@RequestParam String ciId,
                                                                               @RequestParam(required = false) String keyword,
                                                                               @RequestParam String scoreInstanceCode,
                                                                               @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(service.getBranch(keyword, ciId, scoreInstanceCode, pageable));
    }

    @LogActivity(function = "Xuất Excel chi nhánh theo điểm")
    @PostMapping("/export-excel-score-branch")
    public ResponseEntity<byte[]> exportExcelTxnScoreBranch(@RequestParam String ciId,
                                                            @RequestParam(required = false) String keyword,
                                                            @RequestParam String scoreInstanceCode) throws Exception {
        List<BranchScoreCommonInfo> data = service.exportExcelTxnScoreBranch(keyword, ciId, scoreInstanceCode);
        return exportExcel.exportExcel(data, "SmrTxnScoreBranch.xlsx");
    }

    @LogActivity(function = "Lấy chi tiết chi nhánh theo điểm")
    @PostMapping("score-branch-detail/{id}")
    public ResponseEntity<ResponseData<BranchResultDto>> getBranchDetail(@PathVariable Long id) {
        return ResponseData.okEntity(service.getBranchDetail(id));
    }


}
