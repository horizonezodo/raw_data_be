package ngvgroup.com.hrm.feature.cfg_org_unit.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitDTO;
import ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitOptionDto;
import ngvgroup.com.hrm.feature.cfg_org_unit.dto.HrmCfgOrgUnitSearch;
import ngvgroup.com.hrm.feature.cfg_org_unit.model.HrmCfgOrgUnit;
import ngvgroup.com.hrm.feature.cfg_org_unit.service.HrmCfgOrgUnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@LogActivity(function = "Phòng ban")
@RestController
@RequestMapping("/org-unit")
public class HrmCfgOrgUnitController
        extends BaseController<HrmCfgOrgUnit, HrmCfgOrgUnitDTO, HrmCfgOrgUnitService> {

    private final HrmCfgOrgUnitService unitService;
    private final ExportExcel exportExcel;



    public HrmCfgOrgUnitController(HrmCfgOrgUnitService service, ExportExcel exportExcelService) {
        super(service);
        unitService = service;
        exportExcel = exportExcelService;
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData<List<HrmCfgOrgUnitSearch>>> getAllUnit() {
        return ResponseData.okEntity(unitService.findAllUnit());
    }

    @GetMapping("/options")
    public ResponseEntity<ResponseData<List<HrmCfgOrgUnitOptionDto>>> getOptionUnit() {
        return ResponseData.okEntity(unitService.listOptions());
    }

    @LogActivity(function = "Xuất file excel")
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable String fileName) throws Exception {
        return exportExcel.exportExcel(service.findAllUnit(), fileName);
    }
}
