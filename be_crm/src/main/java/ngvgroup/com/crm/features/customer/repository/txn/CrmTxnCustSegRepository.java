package ngvgroup.com.crm.features.customer.repository.txn;

import java.util.Optional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.txn.CrmTxnCustSeg;

import org.springframework.stereotype.Repository;

@Repository
public interface CrmTxnCustSegRepository extends BaseRepository<CrmTxnCustSeg> {

    Optional<CrmTxnCustSeg> findByCustomerCode(String customerCode);

    Optional<CrmTxnCustSeg> findByProcessInstanceCode(String processCode);
}
