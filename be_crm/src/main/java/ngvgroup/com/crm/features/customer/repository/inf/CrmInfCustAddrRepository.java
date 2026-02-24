package ngvgroup.com.crm.features.customer.repository.inf;

import java.util.List;
import java.util.Optional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.inf.CrmInfCustAddr;

import org.springframework.stereotype.Repository;

@Repository
public interface CrmInfCustAddrRepository extends BaseRepository<CrmInfCustAddr> {

    Optional<CrmInfCustAddr> findByCustomerCode(String customerCode);

    List<CrmInfCustAddr> findAllByCustomerCode(String customerCode);
}
