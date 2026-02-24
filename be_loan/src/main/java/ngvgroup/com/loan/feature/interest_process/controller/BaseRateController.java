package ngvgroup.com.loan.feature.interest_process.controller;

import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.loan.feature.interest_process.dto.BaseRateDTO;
import ngvgroup.com.loan.feature.interest_process.model.ComCfgBaseRate;
import ngvgroup.com.loan.feature.interest_process.service.BaseRateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base-rate")
public class BaseRateController extends BaseController<ComCfgBaseRate, BaseRateDTO, BaseRateService> {
    public BaseRateController(BaseRateService service) {
        super(service);
    }
}
