package ngvgroup.com.fac.feature.fac_inf_acc.controller;

import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.CurrencyTypeDto;
import ngvgroup.com.fac.feature.fac_inf_acc.model.ComInfCurrencyType;
import ngvgroup.com.fac.feature.fac_inf_acc.service.ComInfCurrencyTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("currency-type")
public class ComInfCurrencyTypeController extends BaseController<ComInfCurrencyType, CurrencyTypeDto, ComInfCurrencyTypeService> {
    public ComInfCurrencyTypeController(ComInfCurrencyTypeService service) {
        super(service);
    }
}
