package ngvgroup.com.loan.feature.interest_process.repository.cfg;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRateTier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LnmCfgIntRateTierRepository extends BaseRepository<LnmCfgIntRateTier> {

    List<LnmCfgIntRateTier> findAllByInterestRateCode(String interestRateCode);

    void deleteAllByInterestRateCode(String interestRateCode);

    @Modifying
    @Query("""
    update LnmCfgIntRateTier f
    set f.isDelete = 1
    where f.interestRateCode = :interestRateCode
""")
    void softDeleteByInterestRateCode(String interestRateCode);
}