package ngvgroup.com.fac.feature.fac_inf_acc.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacCfgAccPurposeDto;
import ngvgroup.com.fac.feature.fac_inf_acc.mapper.FacCfgAccPurposeMapper;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccPurpose;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacCfgAccPurposeRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.service.FacCfgAccPurposeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FacCfgAccPurposeServiceImpl extends BaseServiceImpl<FacCfgAccPurpose, FacCfgAccPurposeDto> implements FacCfgAccPurposeService {
    private final FacCfgAccPurposeRepository purposeRepo;
    public FacCfgAccPurposeServiceImpl(
            FacCfgAccPurposeRepository purposeRepo,
            FacCfgAccPurposeMapper purposeMapper) {
        super(purposeRepo, purposeMapper);
        this.purposeRepo = purposeRepo;
    }

    @Override
    public Page<FacCfgAccPurposeDto> getPurposes(Pageable pageable) {
        Page<FacCfgAccPurpose> page = purposeRepo.findAll(pageable);
        return page.map(mapper::toDto);
    }
}
