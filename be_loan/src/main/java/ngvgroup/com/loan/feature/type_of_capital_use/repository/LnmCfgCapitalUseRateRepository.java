package ngvgroup.com.loan.feature.type_of_capital_use.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.type_of_capital_use.model.LnmCfgCapitalUseRate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LnmCfgCapitalUseRateRepository extends BaseRepository<LnmCfgCapitalUseRate> {

    @Query("""
    select e.capitalUseCode 
    from LnmCfgCapitalUseRate e 
    where e.capitalUseCode in :codes 
      and e.orgCode = :orgCode
""")
    List<String> findExistingCodes(
            @Param("codes") Set<String> codes,
            @Param("orgCode") String orgCode
    );


    void deleteAllByCapitalUseCodeAndOrgCode(String capitalUseCode,String orgCode);

    List<LnmCfgCapitalUseRate> findAllByCapitalUseCodeAndOrgCode(String capitalCode, String orgCode);
}
