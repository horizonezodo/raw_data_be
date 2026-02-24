package ngvgroup.com.ibm.feature.common.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.ibm.feature.common.dto.CtgInfCurrencyTypeDto;
import ngvgroup.com.ibm.feature.common.repository.CtgInfCurrencyTypeRepository;
import ngvgroup.com.ibm.feature.common.service.CtgInfCurrencyTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CtgInfCurrencyTypeServiceImpl implements CtgInfCurrencyTypeService {

    private final CtgInfCurrencyTypeRepository ctgInfCurrencyTypeRepository;

    @Override
    public List<CtgInfCurrencyTypeDto> getAllCtgInfCurrencyTypes() {
        return ctgInfCurrencyTypeRepository.getAllCtgInfCurrencyTypes();
    }
}
