package ngvgroup.com.fac.feature.common.service.impl;

import lombok.AllArgsConstructor;
import ngvgroup.com.fac.feature.common.dto.BusModuleProcessTypeCodeDto;
import ngvgroup.com.fac.feature.common.dto.ComCfgBusModuleDto;
import ngvgroup.com.fac.feature.common.dto.ComCfgProcessTypeDTO;
import ngvgroup.com.fac.feature.common.dto.ModuleCodeTreeFilter;
import ngvgroup.com.fac.feature.common.repository.ComCfgBusModuleRepository;
import ngvgroup.com.fac.feature.common.service.ComCfgBusModuleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ComCfgBusModuleServiceImpl implements ComCfgBusModuleService {
    private final ComCfgBusModuleRepository comCfgBusModuleRepository;

    @Override
    public List<ComCfgBusModuleDto> getCtgCfgBusModules() {
        return comCfgBusModuleRepository.getCtgCfgBusModules();
    }

    @Override
    public List<BusModuleProcessTypeCodeDto> getBusModulesByProcessTypeCode(String processTypeCode) {
        return comCfgBusModuleRepository.getBusModulesByProcessTypeCode(processTypeCode);
    }

    @Override
    public List<ModuleCodeTreeFilter> getTreeList() {
        List<BusModuleProcessTypeCodeDto> items = comCfgBusModuleRepository.getTreeFilter();
        Map<String, ModuleCodeTreeFilter> map = new LinkedHashMap<>();
        for (BusModuleProcessTypeCodeDto item : items) {
            map.computeIfAbsent(item.getModuleCode(), code -> new ModuleCodeTreeFilter(
                    item.getModuleCode(),
                    item.getModuleName(),
                    new ArrayList<>())).getProcessTypes().add(new ComCfgProcessTypeDTO(
                    item.getProcessTypeCode(),
                    item.getProcessTypeName()));
        }

        return new ArrayList<>(map.values());
    }
}
