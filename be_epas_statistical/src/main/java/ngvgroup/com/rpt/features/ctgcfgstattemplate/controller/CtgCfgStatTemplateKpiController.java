package ngvgroup.com.rpt.features.ctgcfgstattemplate.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.CtgCfgStatTemplateKpiDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.IndexKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.IndexKpiRequestDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTemplateKpiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/template-kpi")
public class CtgCfgStatTemplateKpiController {
    private final CtgCfgStatTemplateKpiService ctgCfgStatTemplateKpiService;

    @LogActivity(function = "Lấy danh sách TEMPLATE_KPI")
    @Operation(summary = "Lấy danh sách TEMPLATE_KPI")
    @GetMapping
    public ResponseEntity<ResponseData<List<CtgCfgStatTemplateKpiDTO>>> getAllByTemplateCode(@RequestParam String templateCode){
        return ResponseData.okEntity(this.ctgCfgStatTemplateKpiService.getAllByTemplateCode(templateCode));
    }

    @LogActivity(function = "Lấy KPI theo chỉ số")
    @PostMapping("/index-kpi")
    public ResponseEntity<ResponseData<List<IndexKpiDto>>> getKpiByIndex(
        @RequestBody List<IndexKpiRequestDto> request
    )
    {
        return ResponseData.okEntity(this.ctgCfgStatTemplateKpiService.getKpiByIndex(
            request
        ));
    }

    @LogActivity(function = "Lấy chi tiết TEMPLATE_KPI")
    @GetMapping("/{templateKpiCode}")
    public ResponseEntity<ResponseData<CtgCfgStatTemplateKpiDTO>> getById(@PathVariable("templateKpiCode")String templateKpiCode){
        return ResponseData.okEntity(this.ctgCfgStatTemplateKpiService.getByTemplateKpiCode(templateKpiCode));
    }
}
