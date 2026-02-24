package ngvgroup.com.fac.feature.fac_inf_acc.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacCfgAccStructureDto;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccStructure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacCfgAccStructureService extends BaseService<FacCfgAccStructure, FacCfgAccStructureDto> {
    Page<FacCfgAccStructureDto> getStructure(Pageable pageable);
    FacCfgAccStructure findByStructureCode(String structureCode);

}
