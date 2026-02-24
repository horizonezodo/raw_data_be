package ngvgroup.com.fac.feature.fac_inf_acc.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacCfgAccStructureDto;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccStructure;
import ngvgroup.com.fac.feature.fac_inf_acc.service.FacCfgAccStructureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("acc-structure")
public class FacCfgAccStructureController extends BaseController<FacCfgAccStructure, FacCfgAccStructureDto, FacCfgAccStructureService> {

    public FacCfgAccStructureController(FacCfgAccStructureService service) {
        super(service);
    }

    @GetMapping("/list-structure")
    public ResponseEntity<ResponseData<Page<FacCfgAccStructureDto>>> getAccStructure (Pageable pageable) {
        return ResponseData.okEntity(service.getStructure(pageable));
    }
    @GetMapping("structure-code")
    public ResponseEntity<ResponseData<FacCfgAccStructure>> findByStructureCode(String structureCode) {
        return ResponseData.okEntity(service.findByStructureCode(structureCode));
    }
}
