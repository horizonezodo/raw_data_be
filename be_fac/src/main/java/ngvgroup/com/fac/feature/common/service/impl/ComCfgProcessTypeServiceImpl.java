package ngvgroup.com.fac.feature.common.service.impl;

import ngvgroup.com.fac.feature.common.dto.ComCfgProcessTypeDTO;
import ngvgroup.com.fac.feature.common.repository.ComCfgProcessTypeRepository;
import ngvgroup.com.fac.feature.common.service.ComCfgProcessTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComCfgProcessTypeServiceImpl implements ComCfgProcessTypeService {
    private final ComCfgProcessTypeRepository cfgProcessTypeRepository;

    public ComCfgProcessTypeServiceImpl(ComCfgProcessTypeRepository cfgProcessTypeRepository) {
        this.cfgProcessTypeRepository = cfgProcessTypeRepository;
    }

    @Override
    public List<ComCfgProcessTypeDTO> getAll() {
        return cfgProcessTypeRepository.getAll();
    }
}
