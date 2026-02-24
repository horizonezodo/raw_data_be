package ngvgroup.com.fac.feature.fac_cfg_voucher.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherMethodGenDTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.mapper.FacCfgVoucherMethodGenMapper;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherMethodGen;
import ngvgroup.com.fac.feature.fac_cfg_voucher.repository.FacCfgVoucherMethodGenRepository;
import ngvgroup.com.fac.feature.fac_cfg_voucher.service.FacCfgVoucherMethodGenService;
import org.springframework.stereotype.Service;

@Service
public class FacCfgVoucherMethodGenServiceImpl
        extends BaseServiceImpl<FacCfgVoucherMethodGen, FacCfgVoucherMethodGenDTO>
        implements FacCfgVoucherMethodGenService {

    public FacCfgVoucherMethodGenServiceImpl(FacCfgVoucherMethodGenRepository methodGenRepository, FacCfgVoucherMethodGenMapper methodGenMapper) {
        super(methodGenRepository, methodGenMapper);
    }
}
