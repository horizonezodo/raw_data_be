package ngvgroup.com.fac.feature.fac_cfg_voucher.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherRuleMapDTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.mapper.FacCfgVoucherRuleMapMapper;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherRuleMap;
import ngvgroup.com.fac.feature.fac_cfg_voucher.repository.FacCfgVoucherRuleMapRepository;
import ngvgroup.com.fac.feature.fac_cfg_voucher.service.FacCfgVoucherRuleMapService;
import org.springframework.stereotype.Service;

@Service
public class FacCfgVoucherRuleMapServiceImpl
        extends BaseServiceImpl<FacCfgVoucherRuleMap, FacCfgVoucherRuleMapDTO>
        implements FacCfgVoucherRuleMapService {

    protected FacCfgVoucherRuleMapServiceImpl(FacCfgVoucherRuleMapRepository repository, FacCfgVoucherRuleMapMapper mapper) {
        super(repository, mapper);
    }
}
