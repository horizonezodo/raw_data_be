package ngvgroup.com.fac.feature.fac_cfg_acct_entry.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.common.dto.CtgCfgCommonDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctEntryDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctEntryResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.search.AcctEntrySearchDetail;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctEntry;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.service.FacCfgAcctEntryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@LogActivity(function = "Bút toán")
@RestController
@RequestMapping("/acct-entry")
public class FacCfgAcctEntryController
        extends BaseController<FacCfgAcctEntry, FacCfgAcctEntryDTO, FacCfgAcctEntryService> {

    public FacCfgAcctEntryController(FacCfgAcctEntryService service) {
        super(service);
    }

    @LogActivity(function = "Thông tin chi tiết")
    @PostMapping("/list-detail")
    public ResponseEntity<ResponseData<Page<FacCfgAcctEntryResDTO>>> getListDetailAcctEntry(
            @RequestBody(required = false) AcctEntrySearchDetail searchDetail,
            Pageable pageable) {

        return ResponseData.okEntity(service.getListDetail(searchDetail, pageable));
    }

    @LogActivity(function = "Lấy thông tin loại phát sinh")
    @GetMapping("/list-entry-type-code")
    public ResponseEntity<ResponseData<List<CtgCfgCommonDTO>>> getListEntryTypeCode(
            @RequestParam(required = false) String moduleCode,
            @RequestParam(required = false) String commonTypeCode
    ) {
        return ResponseData.okEntity(service.getListEntryTypeCode(moduleCode, commonTypeCode));
    }
}
