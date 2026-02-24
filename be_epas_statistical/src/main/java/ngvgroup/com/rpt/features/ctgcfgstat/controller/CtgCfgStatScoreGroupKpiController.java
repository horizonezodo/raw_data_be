package ngvgroup.com.rpt.features.ctgcfgstat.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscoregroupkpi.CtgCfgStatScoreGroupKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstat.service.CtgCfgStatScoreGroupKpiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stat-score-group-kpi")
@RequiredArgsConstructor
public class CtgCfgStatScoreGroupKpiController {
    private final CtgCfgStatScoreGroupKpiService ctgCfgStatScoreGroupKpiService;

    @LogActivity(function = "Tạo mới nhóm điểm KPI")
    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody List<CtgCfgStatScoreGroupKpiDto> ctgCfgStatScoreGroupKpiDtos) {
        ctgCfgStatScoreGroupKpiService.create(ctgCfgStatScoreGroupKpiDtos);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Xóa nhóm điểm KPI")
    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam String statScoreGroupCode) {
        ctgCfgStatScoreGroupKpiService.delete(statScoreGroupCode);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Lấy tất cả nhóm điểm KPI theo mã")
    @GetMapping
    public ResponseEntity<ResponseData<List<CtgCfgStatScoreGroupKpiDto>>>getAllByStatScoreGroupCode(@RequestParam String statScoreGroupCode){
        return ResponseData.okEntity(ctgCfgStatScoreGroupKpiService.getAllByStatScoreGroupCode(statScoreGroupCode));
    }
}
