package ngvgroup.com.loan.feature.interest_process.repository.txn;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LnmTxnIntRateRepository extends BaseRepository<LnmTxnIntRate> {

    boolean existsByInterestRateCode(String interestRateCode);

    Optional<LnmTxnIntRate> findByProcessInstanceCode(String processInstanceCode);

    List<LnmTxnIntRate> findAllByInterestRateCode(String interestRateCode);
}