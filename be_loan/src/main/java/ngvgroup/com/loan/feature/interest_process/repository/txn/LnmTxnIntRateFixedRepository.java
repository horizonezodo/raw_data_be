package ngvgroup.com.loan.feature.interest_process.repository.txn;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRateFixed;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LnmTxnIntRateFixedRepository extends BaseRepository<LnmTxnIntRateFixed> {

    List<LnmTxnIntRateFixed> findAllByInterestRateCode(String interestRateCode);

    List<LnmTxnIntRateFixed> findAllByProcessInstanceCode(String processInstanceCode);

    void deleteAllByInterestRateCode(String interestRateCode);

    @Modifying
    @Query("""
    update LnmCfgIntRateFixed f
    set f.isDelete = 1
    where f.interestRateCode = :interestRateCode
""")
    void softDeleteByInterestRateCode(String interestRateCode);
}