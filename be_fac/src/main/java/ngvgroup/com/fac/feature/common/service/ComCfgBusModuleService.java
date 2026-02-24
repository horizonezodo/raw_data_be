package ngvgroup.com.fac.feature.common.service;

import ngvgroup.com.fac.feature.common.dto.BusModuleProcessTypeCodeDto;
import ngvgroup.com.fac.feature.common.dto.ComCfgBusModuleDto;
import ngvgroup.com.fac.feature.common.dto.ModuleCodeTreeFilter;

import java.util.List;

public interface ComCfgBusModuleService {
    List<ComCfgBusModuleDto> getCtgCfgBusModules();

    List<BusModuleProcessTypeCodeDto> getBusModulesByProcessTypeCode(String processTypeCode);

    List<ModuleCodeTreeFilter> getTreeList();
}
