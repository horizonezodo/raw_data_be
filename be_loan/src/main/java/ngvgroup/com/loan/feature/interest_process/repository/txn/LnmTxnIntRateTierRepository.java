package ngvgroup.com.loan.feature.interest_process.repository.txn;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRateTier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LnmTxnIntRateTierRepository extends BaseRepository<LnmTxnIntRateTier> {

    List<LnmTxnIntRateTier> findAllByInterestRateCode(String interestRateCode);

    List<LnmTxnIntRateTier> findAllByProcessInstanceCode(String processInstanceCode);

    @Modifying
    @Query("""
    update LnmCfgIntRateFixed f
    set f.isDelete = 1
    where f.interestRateCode = :interestRateCode
""")
    void softDeleteByInterestRateCode(String interestRateCode);

    void deleteAllByInterestRateCode(String interestRateCode);
}