package ngvgroup.com.crm.features.customer.repository.inf;

import java.util.Optional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.inf.CrmInfCustIndv;

import org.springframework.stereotype.Repository;

@Repository
public interface CrmInfCustIndvRepository extends BaseRepository<CrmInfCustIndv> {

    Optional<CrmInfCustIndv> findByCustomerCode(String customerCode);
}
