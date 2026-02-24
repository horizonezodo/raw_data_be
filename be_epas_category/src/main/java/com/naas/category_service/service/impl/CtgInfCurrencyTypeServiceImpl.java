package com.naas.category_service.service.impl;

import com.naas.category_service.dto.CtgInfCurrencyType.CtgInfCurrencyTypeDto;
import com.naas.category_service.repository.CtgInfCurrencyTypeRepository;
import com.naas.category_service.service.CtgInfCurrencyTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CtgInfCurrencyTypeServiceImpl implements CtgInfCurrencyTypeService {

    private final CtgInfCurrencyTypeRepository ctgInfCurrencyTypeRepository;

    @Override
    public List<CtgInfCurrencyTypeDto> getAllCtgInfCurrencyTypes(){
        return ctgInfCurrencyTypeRepository.getAllCtgInfCurrencyTypes();
    }


}
