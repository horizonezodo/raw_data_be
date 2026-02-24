package com.naas.admin_service.features.category.service.impl;

import com.naas.admin_service.features.category.dto.CtgInfCurrencyTypeDto;
import com.naas.admin_service.features.category.repository.CtgInfCurrencyTypeRepository;
import com.naas.admin_service.features.category.service.CtgInfCurrencyTypeService;

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
