package com.naas.category_service.service.impl;

import com.naas.category_service.dto.CtgCfgBusModule.CtgCfgBusModuleDto;
import com.naas.category_service.repository.CtgCfgBusModuleRepository;
import com.naas.category_service.service.CtgCfgBusModuleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CtgCfgBusModuleServiceImpl implements CtgCfgBusModuleService {
    private final CtgCfgBusModuleRepository ctgCfgBusModuleRepository;

    @Override
    public List<CtgCfgBusModuleDto> getCtgCfgBusModules(){
        return ctgCfgBusModuleRepository.getCtgCfgBusModules();
    }

}
