package ngvgroup.com.crm.features.customer.repository.txn;

import java.util.Optional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.txn.CrmTxnCustDoc;

import org.springframework.stereotype.Repository;

@Repository
public interface CrmTxnCustDocRepository extends BaseRepository<CrmTxnCustDoc> {

    Optional<CrmTxnCustDoc> findByCustomerCode(String customerCode);

    Optional<CrmTxnCustDoc> findByProcessInstanceCode(String processCode); 
}
