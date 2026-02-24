package ngvgroup.com.fac.feature.fac_inf_acc.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccStructure;
import org.springframework.stereotype.Repository;

@Repository
public interface FacCfgAccStructureRepository extends BaseRepository<FacCfgAccStructure> {
    FacCfgAccStructure findByAccStructureCode(String accCode);
}
