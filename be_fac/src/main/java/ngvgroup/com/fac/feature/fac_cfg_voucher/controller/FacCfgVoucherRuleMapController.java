package ngvgroup.com.fac.feature.fac_cfg_voucher.controller;

import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherRuleMapDTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherRuleMap;
import ngvgroup.com.fac.feature.fac_cfg_voucher.service.FacCfgVoucherRuleMapService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voucher-rule-map")
public class FacCfgVoucherRuleMapController
        extends BaseController<FacCfgVoucherRuleMap, FacCfgVoucherRuleMapDTO, FacCfgVoucherRuleMapService> {

    public FacCfgVoucherRuleMapController(FacCfgVoucherRuleMapService service) {
        super(service);
    }
}
