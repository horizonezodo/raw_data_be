package ngvgroup.com.rpt.features.smrscore.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.smrscore.dto.*;
import ngvgroup.com.rpt.features.smrscore.service.ResultScoreService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("result-score")
public class ResultScoreController {

    private final ResultScoreService service;
    private final ExportExcel exportExcel;

    @LogActivity(function = "Tìm kiếm kết quả chấm điểm")
    @PostMapping("search")
    public ResponseEntity<ResponseData<Page<SmrScoreSearchDto>>> search(@RequestParam String keyword,
                                                                        @RequestBody ReqSmrScoreSearchDto dto,
                                                                        Pageable pageable) {
        return ResponseData.okEntity(service.search(keyword, dto, pageable));
    }

    @LogActivity(function = "Xuất Excel kết quả chấm điểm")
    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestParam String keyword,
                                              @RequestBody ReqSmrScoreSearchDto dto
    ) throws Exception {
        List<SmrScoreExportExcelDto> data = service.exportExcel(keyword, dto);
        return exportExcel.exportExcel(data, "SmrScoreResult.xlsx");
    }

    @LogActivity(function = "Lấy kết quả chi nhánh")
    @PostMapping("branch-result")
    public ResponseEntity<ResponseData<Page<BranchScoreCommonInfo>>> getBranchResult(@RequestParam String ciId,
                                                                                     @RequestParam(required = false) String keyword,
                                                                                     @RequestParam String scoreInstanceCode,
                                                                                     @ParameterObject Pageable pageable) {
        return ResponseData.okEntity(service.getBranchResult(keyword, ciId, scoreInstanceCode, pageable));
    }

    @LogActivity(function = "Xuất Excel kết quả chi nhánh")
    @PostMapping("/export-excel-branch-result")
    public ResponseEntity<byte[]> exportExcelBranchResult(@RequestParam String ciId,
                                                          @RequestParam(required = false) String keyword,
                                                          @RequestParam String scoreInstanceCode
    ) throws Exception {
        List<BranchScoreCommonInfo> data = service.exportExcelBranchResult(keyword, ciId, scoreInstanceCode);
        return exportExcel.exportExcel(data, "SmrScoreBranchResult.xlsx");
    }

    @LogActivity(function = "Lấy chi tiết kết quả chi nhánh")
    @PostMapping("branch-result-detail/{id}")
    public ResponseEntity<ResponseData<BranchResultDto>> getBranchResultDetail(@PathVariable Long id) {
        return ResponseData.okEntity(service.getBranchResultDetail(id));
    }
}
