package ngvgroup.com.fac.feature.fac_inf_acc.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccPurpose;
import org.springframework.stereotype.Repository;

@Repository
public interface FacCfgAccPurposeRepository extends BaseRepository<FacCfgAccPurpose> {
    boolean existsByAccPurposeCode(String accPurposeCode);
}
