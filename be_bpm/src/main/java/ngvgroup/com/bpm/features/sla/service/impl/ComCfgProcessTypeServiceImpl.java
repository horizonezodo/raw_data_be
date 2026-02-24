package ngvgroup.com.bpm.features.sla.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.features.sla.dto.CtgCfgProcessTypeDto;
import ngvgroup.com.bpm.features.sla.repository.ComCfgProcessTypeRepository;
import ngvgroup.com.bpm.features.sla.service.ComCfgProcessTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComCfgProcessTypeServiceImpl implements ComCfgProcessTypeService {
    private final ComCfgProcessTypeRepository comCfgProcessTypeRepository;

    @Override
    public List<CtgCfgProcessTypeDto> getAll() {
        return comCfgProcessTypeRepository.getAllProcess();
    }
}
