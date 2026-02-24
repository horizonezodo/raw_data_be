package ngvgroup.com.crm.features.customer.repository.txn;

import java.util.Optional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.txn.CrmTxnCustCorp;

import org.springframework.stereotype.Repository;

@Repository
public interface CrmTxnCustCorpRepository extends BaseRepository<CrmTxnCustCorp> {

    Optional<CrmTxnCustCorp> findByCustomerCode(String customerCode);

    Optional<CrmTxnCustCorp> findByProcessInstanceCode(String processCode);
}
