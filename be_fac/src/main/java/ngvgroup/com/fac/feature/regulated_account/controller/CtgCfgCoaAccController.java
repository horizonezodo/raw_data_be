package ngvgroup.com.fac.feature.regulated_account.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.regulated_account.dto.CoaAccFilter;
import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaAccDTO;
import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaAccResDTO;
import ngvgroup.com.fac.feature.regulated_account.model.CtgCfgCoaAcc;
import ngvgroup.com.fac.feature.regulated_account.service.CtgCfgCoaAccService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coa-acc")

public class CtgCfgCoaAccController extends BaseController<CtgCfgCoaAcc, CtgCfgCoaAccDTO, CtgCfgCoaAccService> {
    private final CtgCfgCoaAccService ctgCfgCoaAccService;

    public CtgCfgCoaAccController(CtgCfgCoaAccService service) {
        super(service);
        this.ctgCfgCoaAccService = service;
    }

    @PostMapping("/search")
    public ResponseEntity<ResponseData<Page<CtgCfgCoaAccResDTO>>> search(
            @RequestBody CoaAccFilter filter,
            Pageable pageable) {
        return ResponseData.okEntity(ctgCfgCoaAccService.pageCoaAcc(filter, pageable));
    }

    @PostMapping("/export-excel")
    public ResponseEntity<byte[]> exportToExcel( @RequestParam String fileName,@RequestParam List<String> isInternals){
        return ctgCfgCoaAccService.exportToExcel(fileName,isInternals);
    }

    @GetMapping("/get-by-acc-coa-code")
    public ResponseEntity<ResponseData<CtgCfgCoaAccDTO>> getByAccCoaCode(@RequestParam String accCoaCode){
        return ResponseData.okEntity(ctgCfgCoaAccService.getByAccCoaCode(accCoaCode));
    }

    @GetMapping("/get-by-is-internal")
    ResponseEntity<ResponseData<List<CtgCfgCoaAccDTO>>> getAllByIsInternal(@RequestParam String isInternal,@RequestParam(required = false) String accCoaCode){
        return ResponseData.okEntity(ctgCfgCoaAccService.getAllByIsInternal(isInternal,accCoaCode));
    }

    @GetMapping("/get-by-parent-code")
    ResponseEntity<ResponseData<CtgCfgCoaAccDTO>> getByAccCoaCodeAndIsInternal(@RequestParam String accCoaCode,@RequestParam String isInternal){
        return ResponseData.okEntity(ctgCfgCoaAccService.getByAccCoaCodeAndIsInternal(accCoaCode,isInternal));
    }
}
