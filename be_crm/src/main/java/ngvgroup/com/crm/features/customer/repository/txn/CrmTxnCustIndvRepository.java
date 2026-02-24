package ngvgroup.com.crm.features.customer.repository.txn;

import java.util.Optional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.txn.CrmTxnCustIndv;

import org.springframework.stereotype.Repository;

@Repository
public interface CrmTxnCustIndvRepository extends BaseRepository<CrmTxnCustIndv> {

    Optional<CrmTxnCustIndv> findByCustomerCode(String customerCode);

    Optional<CrmTxnCustIndv> findByProcessInstanceCode(String processCode);
}
