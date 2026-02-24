package ngvgroup.com.fac.feature.ctg_cfg_acc_class.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.model.FacCfgAccClass;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.service.FacCfgAccClassService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("class")

public class FacCfgAccClassController extends BaseController<FacCfgAccClass, FacCfgAccClassDto, FacCfgAccClassService> {

    private final FacCfgAccClassService ctgCfgAccClassService;

    public FacCfgAccClassController(FacCfgAccClassService service) {
        super(service);
        this.ctgCfgAccClassService = service;
    }

    @GetMapping("/search")
    ResponseEntity<ResponseData<Page<FacCfgAccClassDto>>> searchAll(@RequestParam(required = false) String keyword,     Pageable pageable){
        return ResponseData.okEntity(ctgCfgAccClassService.searchAll(keyword,pageable));
    }

    @GetMapping("/export-to-excel")
    ResponseEntity<byte[]> exportToExcel(@RequestParam String keyword,@RequestParam String fileName){
        return ctgCfgAccClassService.exportToExcel(keyword,fileName);
    }

    @GetMapping("/check-exists")
    ResponseEntity<ResponseData<Boolean>> checkExist(@RequestParam String accClassCode){
        return ResponseData.okEntity(ctgCfgAccClassService.existsByAccClassCode(accClassCode));
    }

    @GetMapping("/get-by-acc-side-type")
    public ResponseEntity<ResponseData<List<FacCfgAccClassDto>>> getByAccSideType(
            @RequestParam(required = false) String accSideType){
        return ResponseData.okEntity(ctgCfgAccClassService.findAllByAccSideType(accSideType));
    }
}
