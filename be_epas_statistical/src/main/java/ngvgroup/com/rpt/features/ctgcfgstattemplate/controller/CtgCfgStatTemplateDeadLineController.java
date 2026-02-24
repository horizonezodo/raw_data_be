package ngvgroup.com.rpt.features.ctgcfgstattemplate.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatedeadline.CtgCfgStatTemplateDeadLineDTO;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.service.CtgCfgStatTemplateDeadLineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dead-line")
@RequiredArgsConstructor
public class CtgCfgStatTemplateDeadLineController {
    private final CtgCfgStatTemplateDeadLineService service;

    @LogActivity(function = "Lấy thông tin deadline theo mẫu biểu")
    @GetMapping
    public ResponseEntity<ResponseData<CtgCfgStatTemplateDeadLineDTO>>getAllByTemplateCode(@RequestParam String templateCode){
        return ResponseData.okEntity(service.getAllByTemplateCode(templateCode));
    }

}
