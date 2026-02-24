package ngvgroup.com.crm.features.customer.repository.inf;

import java.util.Optional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.inf.CrmInfCustCorp;

import org.springframework.stereotype.Repository;

@Repository
public interface CrmInfCustCorpRepository extends BaseRepository<CrmInfCustCorp> {

    Optional<CrmInfCustCorp> findByCustomerCode(String customerCode);
}
