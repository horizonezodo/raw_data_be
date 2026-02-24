package ngvgroup.com.fac.feature.single_entry_acct.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntry;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacTxnAcctEntryRepository extends BaseRepository<FacTxnAcctEntry> {
    Optional<FacTxnAcctEntry> findByProcessInstanceCode(String processInstanceCode);
}
