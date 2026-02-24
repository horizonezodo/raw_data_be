package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.model.IbmCfgDepIntRate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IbmCfgDepIntRateRepository extends BaseRepository<IbmCfgDepIntRate> {
    boolean existsByInterestRateCode(String interestRateCode);
    List<IbmCfgDepIntRate> findAllByOrderByModifiedDateDesc();
}
