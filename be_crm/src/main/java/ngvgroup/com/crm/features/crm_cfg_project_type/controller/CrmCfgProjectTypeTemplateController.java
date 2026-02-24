package ngvgroup.com.crm.features.crm_cfg_project_type.controller;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import lombok.AllArgsConstructor;
import ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeTemplateDto;
import ngvgroup.com.crm.features.crm_cfg_project_type.service.CrmCfgProjectTypeTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("project-type-template")
@AllArgsConstructor
public class CrmCfgProjectTypeTemplateController {

    private final CrmCfgProjectTypeTemplateService crmCfgProjectTypeTemplateService;

    @GetMapping("/search-all-by-project-type-code")
    public ResponseEntity<ResponseData<Page<CrmCfgProjectTypeTemplateDto>>> search(@RequestParam String keyword, @RequestParam String projectTypeCode, Pageable pageable) {
        return ResponseData.okEntity(crmCfgProjectTypeTemplateService.searchAllByProjectTypeCode(keyword,projectTypeCode,pageable));
    }

    @PostMapping
    public ResponseEntity<ResponseData<Void>> create(@RequestBody List<CrmCfgProjectTypeTemplateDto> crmCfgProjectTypeTemplateDtos){
        crmCfgProjectTypeTemplateService.create(crmCfgProjectTypeTemplateDtos);
        return ResponseData.okEntity();
    }

    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@RequestParam String projectTypeCode,@RequestBody List<CrmCfgProjectTypeTemplateDto> crmCfgProjectTypeTemplateDtos){
        crmCfgProjectTypeTemplateService.update(projectTypeCode,crmCfgProjectTypeTemplateDtos);
        return ResponseData.okEntity();
    }

    @DeleteMapping
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam String projectTypeCode){
        crmCfgProjectTypeTemplateService.deleteAllByProjectTypeCode(projectTypeCode);
        return ResponseData.okEntity();
    }
}