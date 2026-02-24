package ngvgroup.com.fac.feature.fac_cfg_voucher.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherRuleN0DTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.mapper.FacCfgVoucherRuleN0Mapper;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherRuleN0;
import ngvgroup.com.fac.feature.fac_cfg_voucher.repository.FacCfgVoucherRuleN0Repository;
import ngvgroup.com.fac.feature.fac_cfg_voucher.service.FacCfgVoucherRuleN0Service;
import org.springframework.stereotype.Service;

@Service
public class FacCfgVoucherRuleN0ServiceImpl
        extends BaseServiceImpl<FacCfgVoucherRuleN0, FacCfgVoucherRuleN0DTO>
        implements FacCfgVoucherRuleN0Service {
    private final FacCfgVoucherRuleN0Repository voucherRuleN0Repo;

    protected FacCfgVoucherRuleN0ServiceImpl(FacCfgVoucherRuleN0Repository repository, FacCfgVoucherRuleN0Mapper mapper, FacCfgVoucherRuleN0Repository voucherRuleN0Repo) {
        super(repository, mapper);
        this.voucherRuleN0Repo = voucherRuleN0Repo;
    }

    @Override
    public FacCfgVoucherRuleN0 findByVoucherTypeCode(String voucherTypeCode) {
        return voucherRuleN0Repo.findByVoucherTypeCode(voucherTypeCode)
                .orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND));
    }
}
