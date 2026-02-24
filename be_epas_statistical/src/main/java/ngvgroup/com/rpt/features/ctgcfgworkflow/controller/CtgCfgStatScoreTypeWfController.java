package ngvgroup.com.rpt.features.ctgcfgworkflow.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.CtgCfgStatScoreTypeWfDto;
import ngvgroup.com.rpt.features.ctgcfgworkflow.service.CtgCfgStatScoreTypeWfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stat-score-type-wf")
@RequiredArgsConstructor
public class CtgCfgStatScoreTypeWfController {
    private final CtgCfgStatScoreTypeWfService ctgCfgStatScoreTypeWfService;

    @LogActivity(function = "Tạo mới loại điểm workflow")
    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody CtgCfgStatScoreTypeWfDto ctgCfgStatScoreTypeWfDto) {
        ctgCfgStatScoreTypeWfService.create(ctgCfgStatScoreTypeWfDto);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Cập nhật loại điểm workflow")
    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@RequestBody CtgCfgStatScoreTypeWfDto ctgCfgStatScoreTypeWfDto) {
        ctgCfgStatScoreTypeWfService.update(ctgCfgStatScoreTypeWfDto);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Lấy chi tiết loại điểm workflow")
    @GetMapping("/get-detail")
    public ResponseEntity<ResponseData<CtgCfgStatScoreTypeWfDto>> getDetail(@RequestParam String statScoreTypeCode) {

        return ResponseData.okEntity(ctgCfgStatScoreTypeWfService.getDetail(statScoreTypeCode));
    }

    @LogActivity(function = "Xóa loại điểm workflow")
    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam String statScoreTypeCode) {
        ctgCfgStatScoreTypeWfService.delete(statScoreTypeCode);
        return ResponseData.okEntity();
    }


}
