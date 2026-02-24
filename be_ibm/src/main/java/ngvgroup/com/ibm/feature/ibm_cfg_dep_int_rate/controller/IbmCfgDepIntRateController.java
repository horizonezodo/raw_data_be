package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto.IbmCfgDepIntRateDTO;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto.IbmCfgDepIntRateSearch;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.model.IbmCfgDepIntRate;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.service.IbmCfgDepIntRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@LogActivity(function = "Lãi suất tiền gửi TCTD khác")
@RestController
@RequestMapping("/cfg-dep-int-rate")
public class IbmCfgDepIntRateController extends BaseController<IbmCfgDepIntRate, IbmCfgDepIntRateDTO, IbmCfgDepIntRateService> {
    private final IbmCfgDepIntRateService cfgDepIntRateService;
    private final ExportExcel exportExcel;
    public IbmCfgDepIntRateController(IbmCfgDepIntRateService service, ExportExcel exportExcel) {
        super(service);
        cfgDepIntRateService = service;
        this.exportExcel = exportExcel;
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData<List<IbmCfgDepIntRateSearch>>> getAllUnit() {
        return ResponseData.okEntity(cfgDepIntRateService.searchCfgDepIntRate());
    }

    @LogActivity(function = "Xuất file excel")
    @PostMapping("/export-excel/{fileName}")
    public ResponseEntity<byte[]> exportToExcel(@PathVariable String fileName) throws Exception {
        return exportExcel.exportExcel(cfgDepIntRateService.exportExcel(), fileName);
    }
}
