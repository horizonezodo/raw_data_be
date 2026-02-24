package ngvgroup.com.fac.feature.fac_inf_acc.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacCfgAccStructureDto;
import ngvgroup.com.fac.feature.fac_inf_acc.mapper.FacCfgAccStructureMapper;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccStructure;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacCfgAccStructureRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.service.FacCfgAccStructureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FacCfgAccStructureServiceImpl extends BaseServiceImpl<FacCfgAccStructure, FacCfgAccStructureDto> implements FacCfgAccStructureService {

    private final FacCfgAccStructureRepository accStructureRepository;

    public FacCfgAccStructureServiceImpl(FacCfgAccStructureRepository accStructureRepository, FacCfgAccStructureMapper accStructureMapper) {
        super(accStructureRepository, accStructureMapper);
        this.accStructureRepository = accStructureRepository;
    }

    @Override
    public Page<FacCfgAccStructureDto> getStructure(Pageable pageable) {
        Page<FacCfgAccStructure> page = accStructureRepository.findAll(pageable);
        return page.map(mapper::toDto);
    }

    @Override
    public FacCfgAccStructure findByStructureCode(String structureCode) {
        return accStructureRepository.findByAccStructureCode(structureCode);
    }
}
