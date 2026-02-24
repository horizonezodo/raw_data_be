package ngvgroup.com.fac.feature.fac_cfg_voucher.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherRuleN0DTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherRuleN0;

public interface FacCfgVoucherRuleN0Service extends BaseService<FacCfgVoucherRuleN0, FacCfgVoucherRuleN0DTO> {
    FacCfgVoucherRuleN0 findByVoucherTypeCode(String voucherTypeCode);
}
