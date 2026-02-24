package ngvgroup.com.crm.features.customer.repository.txn;

import java.util.List;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.txn.CrmTxnCustAddr;

import org.springframework.stereotype.Repository;

@Repository
public interface CrmTxnCustAddrRepository extends BaseRepository<CrmTxnCustAddr> {

    List<CrmTxnCustAddr> findAllByCustomerCode(String customerCode);
    
    void deleteAllByCustomerCode(String customerCode);
    
    List<CrmTxnCustAddr> findAllByProcessInstanceCode(String processInstanceCode);
}