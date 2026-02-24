package ngvgroup.com.fac.feature.fac_cfg_voucher.controller;

import com.ngvgroup.bpm.core.web.controller.BaseController;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherPrintDTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherPrint;
import ngvgroup.com.fac.feature.fac_cfg_voucher.service.FacCfgVoucherPrintService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voucher-print")
public class FacCfgVoucherPrintController
        extends BaseController<FacCfgVoucherPrint, FacCfgVoucherPrintDTO, FacCfgVoucherPrintService> {

    public FacCfgVoucherPrintController(FacCfgVoucherPrintService service) {
        super(service);
    }
}
