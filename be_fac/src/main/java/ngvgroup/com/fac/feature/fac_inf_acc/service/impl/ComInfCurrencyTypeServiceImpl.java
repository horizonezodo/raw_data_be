package ngvgroup.com.fac.feature.fac_inf_acc.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.CurrencyTypeDto;
import ngvgroup.com.fac.feature.fac_inf_acc.mapper.ComInfCurrencyTypeMapper;
import ngvgroup.com.fac.feature.fac_inf_acc.model.ComInfCurrencyType;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.ComInfCurrencyTypeRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.service.ComInfCurrencyTypeService;
import org.springframework.stereotype.Service;

@Service
public class ComInfCurrencyTypeServiceImpl extends BaseServiceImpl<ComInfCurrencyType, CurrencyTypeDto> implements ComInfCurrencyTypeService {
    protected ComInfCurrencyTypeServiceImpl(ComInfCurrencyTypeRepository typeRepo, ComInfCurrencyTypeMapper typeMapper) {
        super(typeRepo, typeMapper);
    }
}
