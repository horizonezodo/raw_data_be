package ngvgroup.com.crm.features.crm_cfg_project_type.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.crm.features.crm_cfg_project_type.dto.CrmCfgProjectTypeDto;
import ngvgroup.com.crm.features.crm_cfg_project_type.model.CrmCfgProjectType;
import ngvgroup.com.crm.features.crm_cfg_project_type.service.CrmCfgProjectTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("project-type")
public class CrmCfgProjectTypeController extends BaseController<CrmCfgProjectType, CrmCfgProjectTypeDto, CrmCfgProjectTypeService> {

    private final CrmCfgProjectTypeService crmCfgProjectTypeService;

    public CrmCfgProjectTypeController(CrmCfgProjectTypeService service) {
        super(service);
        this.crmCfgProjectTypeService = service;
    }

    @GetMapping("/search-all")
    public ResponseEntity<ResponseData<Page<CrmCfgProjectTypeDto>>> search(@RequestParam String keyword, Pageable pageable) {
        return ResponseData.okEntity(crmCfgProjectTypeService.search(keyword,pageable));
    }

    @GetMapping("/export-to-excel")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam(required = false) String keyword,
                                                @RequestParam String fileName) {
        return crmCfgProjectTypeService.exportToExcel(keyword, fileName);
    }

    @GetMapping("/check-exists")
    public ResponseEntity<ResponseData<Boolean>> existsByProjectTypeCode(@RequestParam String projectTypeCode) {
        return ResponseData.okEntity(crmCfgProjectTypeService.existsByProjectTypeCode(projectTypeCode));
    }
}