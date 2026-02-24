package com.naas.admin_service.features.common.service.impl;

import com.naas.admin_service.features.common.dto.ComCfgBusModuleDto;
import com.naas.admin_service.features.common.repository.ComCfgBusModuleRepository;
import com.naas.admin_service.features.common.service.ComCfgBusModuleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ComCfgBusModuleServiceImpl implements ComCfgBusModuleService {
    private final ComCfgBusModuleRepository comCfgBusModuleRepository;

    @Override
    public List<ComCfgBusModuleDto> getCtgCfgBusModules() {
        return comCfgBusModuleRepository.getCtgCfgBusModules();
    }
}
