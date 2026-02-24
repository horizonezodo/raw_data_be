    package ngvgroup.com.rpt.features.ctgcfgstat.controller;

    import com.ngvgroup.bpm.core.common.dto.ResponseData;
    import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
    import lombok.RequiredArgsConstructor;
    import ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscorekpiresult.CtgCfgStatScoreKpiResultDto;
    import ngvgroup.com.rpt.features.ctgcfgstat.service.CtgCfgStatScoreKpiResultService;
    import org.springdoc.core.annotations.ParameterObject;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/stat-score-kpi-result")
    @RequiredArgsConstructor
    public class CtgCfgStatScoreKpiResultController {
        private final CtgCfgStatScoreKpiResultService ctgCfgStatScoreKpiResultService;

        @LogActivity(function = "Tìm kiếm kết quả điểm KPI")
        @PostMapping("/search-all")
        public ResponseEntity<ResponseData<Page<CtgCfgStatScoreKpiResultDto>>> searchAll(@RequestParam String kpiCode,@RequestParam String keyword, @ParameterObject Pageable pageable) {

            return ResponseData.okEntity(ctgCfgStatScoreKpiResultService.searchAll(kpiCode,keyword,pageable));
        }


        @LogActivity(function = "Tạo mới kết quả điểm KPI")
        @PostMapping
        public ResponseEntity<ResponseData<Void>> create(@RequestBody List<CtgCfgStatScoreKpiResultDto> ctgCfgStatScoreKpiResultDtos) {
            ctgCfgStatScoreKpiResultService.create(ctgCfgStatScoreKpiResultDtos);
            return ResponseData.okEntity();
        }



        @LogActivity(function = "Lấy chi tiết kết quả điểm KPI")
        @GetMapping("/get-detail")
        public ResponseEntity<ResponseData<CtgCfgStatScoreKpiResultDto>> getDetail(@RequestParam Long id) {

            return ResponseData.okEntity(ctgCfgStatScoreKpiResultService.getDetail(id));
        }


    }
