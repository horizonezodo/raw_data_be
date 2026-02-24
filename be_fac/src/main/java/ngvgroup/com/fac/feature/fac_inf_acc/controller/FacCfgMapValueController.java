package ngvgroup.com.fac.feature.fac_inf_acc.controller;

import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.AccMapValueDto;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccMapValue;
import ngvgroup.com.fac.feature.fac_inf_acc.service.FacCfgAccMapValueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cfg-map-value")
public class FacCfgMapValueController extends BaseController<FacCfgAccMapValue, AccMapValueDto, FacCfgAccMapValueService> {
    public FacCfgMapValueController(FacCfgAccMapValueService service) {
        super(service);
    }

    @GetMapping("/domain")
    public ResponseEntity<AccMapValueDto> getDomainConfig() {
        return ResponseEntity.ok(service.getDomainConfig());
    }
}
