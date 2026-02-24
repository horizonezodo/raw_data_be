package ngvgroup.com.loan.feature.interest_process.repository.cfg;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRateFixed;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LnmCfgIntRateFixedRepository extends BaseRepository<LnmCfgIntRateFixed> {

    List<LnmCfgIntRateFixed> findAllByInterestRateCode(String interestRateCode);

    void deleteAllByInterestRateCode(String interestRateCode);

    @Modifying
    @Query("""
    update LnmCfgIntRateFixed f
    set f.isDelete = 1
    where f.interestRateCode = :interestRateCode
""")
    void softDeleteByInterestRateCode(String interestRateCode);
}