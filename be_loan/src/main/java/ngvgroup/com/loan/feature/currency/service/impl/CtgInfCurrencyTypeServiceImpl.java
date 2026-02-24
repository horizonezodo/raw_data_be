package ngvgroup.com.loan.feature.currency.service.impl;


import lombok.AllArgsConstructor;
import ngvgroup.com.loan.feature.currency.dto.CtgInfCurrencyTypeDto;
import ngvgroup.com.loan.feature.currency.feign.CurrencyFeign;
import ngvgroup.com.loan.feature.currency.service.CtgInfCurrencyTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CtgInfCurrencyTypeServiceImpl implements CtgInfCurrencyTypeService {
    private final CurrencyFeign feign;

    @Override
    public List<CtgInfCurrencyTypeDto> getAllCtgInfCurrencyTypes(){
        var lst = feign.getAll();
        return lst.getData();
    }


}
