package ngvgroup.com.fac.feature.fac_inf_acc.controller;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacCfgAccPurposeDto;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccPurpose;
import ngvgroup.com.fac.feature.fac_inf_acc.service.FacCfgAccPurposeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("acc-purpose")
public class FacCfgAccPurposeController extends BaseController<FacCfgAccPurpose, FacCfgAccPurposeDto, FacCfgAccPurposeService> {
    public FacCfgAccPurposeController(FacCfgAccPurposeService service) {
        super(service);
    }

    @GetMapping("search")
    public ResponseEntity<ResponseData<Page<FacCfgAccPurposeDto>>> getDomainConfig(Pageable pageable) {
        return ResponseData.okEntity(service.getPurposes(pageable));
    }
}
