package ngvgroup.com.fac.feature.fac_cfg_voucher.controller;

import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherRuleN0DTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherRuleN0;
import ngvgroup.com.fac.feature.fac_cfg_voucher.service.FacCfgVoucherRuleN0Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voucher-rule-no")
public class FacCfgVoucherRuleN0Controller
        extends BaseController<FacCfgVoucherRuleN0, FacCfgVoucherRuleN0DTO, FacCfgVoucherRuleN0Service> {

    public FacCfgVoucherRuleN0Controller(FacCfgVoucherRuleN0Service service) {
        super(service);
    }
}
