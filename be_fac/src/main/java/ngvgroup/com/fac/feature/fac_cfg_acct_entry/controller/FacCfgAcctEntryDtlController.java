package ngvgroup.com.fac.feature.fac_cfg_acct_entry.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctEntryDtlDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctEntryDtlResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctEntryDtl;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.service.FacCfgAcctEntryDtlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@LogActivity(function = "Bút toán")
@RestController
@RequestMapping("/acct-entry-dtl")
public class FacCfgAcctEntryDtlController
        extends BaseController<FacCfgAcctEntryDtl, FacCfgAcctEntryDtlDTO, FacCfgAcctEntryDtlService> {


    public FacCfgAcctEntryDtlController(FacCfgAcctEntryDtlService service) {
        super(service);
    }

    @LogActivity(function = "Danh sách chi tiết")
    @PostMapping("/get-list-detail")
    public ResponseEntity<ResponseData<List<FacCfgAcctEntryDtlResDTO>>> getListDetailDtl(
            @RequestParam String entryCode
    ) {
        return ResponseData.okEntity(service.getListEntryDetail(entryCode));
    }

    @GetMapping("/get-acc-class-code")
    public ResponseEntity<ResponseData<List<String>>> getAccClassCode(
            @RequestParam(required = false) String entryTypeCode,
            @RequestParam(required = false) String entryType) {
        return ResponseData.okEntity(service.getAccClassCode(entryTypeCode, entryType));
    }
}
