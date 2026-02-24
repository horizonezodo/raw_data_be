package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.model.IbmCfgDepIntRateDtl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IbmCfgDepIntRateDtlRepository extends BaseRepository<IbmCfgDepIntRateDtl>  {
    void deleteByInterestRateCode(String interestRateCode);
    List<IbmCfgDepIntRateDtl> findByInterestRateCode(String interestRateCode);
    List<IbmCfgDepIntRateDtl> findByIdIn(List<Long> ids);

    @Modifying
    void deleteByInterestRateCodeAndIdNotIn(String interestRateCode, Collection<Long> id);
}
