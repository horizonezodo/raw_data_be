package ngvgroup.com.fac.feature.fac_cfg_acct_entry.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import jakarta.validation.Valid;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctProcessDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.response.FacCfgAcctProcessResDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.search.FacCfgAcctEntrySearch;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctProcess;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.service.FacCfgAcctProcessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@LogActivity(function = "Bút toán")
@RestController
@RequestMapping("/acct-process")
public class FacCfgAcctProcessController
        extends BaseController<FacCfgAcctProcess, FacCfgAcctProcessDTO, FacCfgAcctProcessService> {

    private final ExportExcel exportExcel;

    public FacCfgAcctProcessController(FacCfgAcctProcessService service, ExportExcel exportExcel) {
        super(service);
        this.exportExcel = exportExcel;
    }

    @LogActivity(function = "Thông tin bút toán")
    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<FacCfgAcctProcessResDTO>>> search(
            @RequestBody(required = false) FacCfgAcctEntrySearch request,
            Pageable pageable) {
        return ResponseData.okEntity(service.getGeneralList(request, pageable));
    }

    @PostMapping("/add-all")
    @LogActivity(function = "Thêm mới bút toán")
    public ResponseEntity<ResponseData<FacCfgAcctProcessDTO>> addListAcctEntry(
            @Valid @RequestBody(required = false) FacCfgAcctProcessDTO dto) {
        return ResponseData.okEntity(service.addProcess(dto));
    }

    @LogActivity(function = "Xuất file excel")
    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws Exception {

        return exportExcel.exportExcel(service.exportToExcel(), "Thông_tin_bút_toán.xlsx");
    }

    @LogActivity(function = "Xóa thông tin bút toán")
    @DeleteMapping("/delete-list")
    public ResponseEntity<ResponseData<Void>> delete(@RequestParam(required = false) String processTypeCode,
                                                     @RequestParam(required = false) String orgCode
                                                     ) {
        service.deleteAcctProcess(processTypeCode, orgCode);
        return ResponseData.okEntity();
    }

    @LogActivity(function = "Cập nhật thông tin bút toán")
    @PutMapping("/update-process")
    public ResponseEntity<ResponseData<FacCfgAcctProcessDTO>> updateAcctProcess(
            @Valid @RequestBody(required = false) FacCfgAcctProcessDTO processDTO) {
        return ResponseData.okEntity(service.updateAcctProcess(processDTO));
    }

    @GetMapping("/exist-process")
    public ResponseEntity<ResponseData<Boolean>> checkExistProcess(
            @RequestParam(required = false) String processTypeCode,
            @RequestParam(required = false) String orgCode,
            @RequestParam(required = false) Boolean isApplyAll) {
        return ResponseData.okEntity(service.checkExistProcess(processTypeCode, orgCode, isApplyAll));
    }
}
