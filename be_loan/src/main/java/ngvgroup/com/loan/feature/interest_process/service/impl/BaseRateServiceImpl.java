package ngvgroup.com.loan.feature.interest_process.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;

import ngvgroup.com.loan.feature.interest_process.dto.BaseRateDTO;
import ngvgroup.com.loan.feature.interest_process.mapper.BaseRateMapper;
import ngvgroup.com.loan.feature.interest_process.model.ComCfgBaseRate;
import ngvgroup.com.loan.feature.interest_process.repository.ComCfgBaseRateRepository;
import ngvgroup.com.loan.feature.interest_process.service.BaseRateService;
import org.springframework.stereotype.Service;

@Service
public class BaseRateServiceImpl extends BaseServiceImpl<ComCfgBaseRate, BaseRateDTO> implements BaseRateService {
    protected BaseRateServiceImpl(ComCfgBaseRateRepository repository, BaseRateMapper mapper) {
        super(repository, mapper);
    }
}

