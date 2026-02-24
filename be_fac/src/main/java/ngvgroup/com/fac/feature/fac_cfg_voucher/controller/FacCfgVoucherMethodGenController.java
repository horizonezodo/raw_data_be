package ngvgroup.com.fac.feature.fac_cfg_voucher.controller;

import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherMethodGenDTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherMethodGen;
import ngvgroup.com.fac.feature.fac_cfg_voucher.service.FacCfgVoucherMethodGenService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voucher-method-gen")
public class FacCfgVoucherMethodGenController
        extends BaseController<FacCfgVoucherMethodGen, FacCfgVoucherMethodGenDTO, FacCfgVoucherMethodGenService> {

    public FacCfgVoucherMethodGenController(FacCfgVoucherMethodGenService service) {
        super(service);
    }
}
