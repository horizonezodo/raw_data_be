package ngvgroup.com.fac.feature.fac_cfg_voucher.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherPrintDTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.mapper.FacCfgVoucherPrintMapper;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherPrint;
import ngvgroup.com.fac.feature.fac_cfg_voucher.repository.FacCfgVoucherPrintRepository;
import ngvgroup.com.fac.feature.fac_cfg_voucher.service.FacCfgVoucherPrintService;
import org.springframework.stereotype.Service;

@Service
public class FacCfgVoucherPrintServiceImpl
        extends BaseServiceImpl<FacCfgVoucherPrint, FacCfgVoucherPrintDTO>
        implements FacCfgVoucherPrintService {

    protected FacCfgVoucherPrintServiceImpl(FacCfgVoucherPrintRepository repository, FacCfgVoucherPrintMapper mapper) {
        super(repository, mapper);
    }
}
