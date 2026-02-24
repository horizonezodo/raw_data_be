package ngvgroup.com.rpt.features.ctgcfgstatrulemapping.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstatrulemapping.dto.RuleMappingDto;
import ngvgroup.com.rpt.features.ctgcfgstatrulemapping.model.StatRuleDefineKpi;
import ngvgroup.com.rpt.features.ctgcfgstatrulemapping.service.StatRuleDefineKpiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/define-kpi-mapping")
@RequiredArgsConstructor
public class StatRuleDefineKpiController {

    private final StatRuleDefineKpiService service;

    @LogActivity(function = "Lưu và cập nhật mapping quy tắc KPI")
    @PostMapping()
    public ResponseEntity<ResponseData<List<StatRuleDefineKpi>>> saveAndUpdate(
            @RequestBody RuleMappingDto mappingDto) {
        return ResponseData.okEntity(service.saveAndUpdate(mappingDto));
    }

    @LogActivity(function = "Lấy danh sách mapping quy tắc KPI")
    @GetMapping()
    public ResponseEntity<ResponseData<List<StatRuleDefineKpi>>> get(
            @RequestParam String templateCode,@RequestParam String kpiCode) {
        return ResponseData.okEntity(service.getList(templateCode,kpiCode));
    }

    @LogActivity(function = "Xóa mapping quy tắc KPI")
    @DeleteMapping()
    public ResponseEntity<ResponseData<String>> delete(
            @RequestParam String templateCode,@RequestParam String kpiCode) {
        service.deleteByTemplateCodeAndKpiCode(templateCode,kpiCode);
        return ResponseData.okEntity("Success");
    }
}
