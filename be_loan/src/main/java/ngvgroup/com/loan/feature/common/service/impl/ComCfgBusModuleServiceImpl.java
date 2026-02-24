package ngvgroup.com.loan.feature.common.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.feature.common.dto.ComCfgBusModuleDto;
import ngvgroup.com.loan.feature.common.feign.ComCfgBusModuleFeign;
import ngvgroup.com.loan.feature.common.service.ComCfgBusModuleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComCfgBusModuleServiceImpl implements ComCfgBusModuleService {

    private final ComCfgBusModuleFeign comCfgBusModuleFeign;

    @Override
    public List<ComCfgBusModuleDto> getALlCtgCfgBusModule() {
        var lst = comCfgBusModuleFeign.getAll();
        return lst.getData();
    }
}
