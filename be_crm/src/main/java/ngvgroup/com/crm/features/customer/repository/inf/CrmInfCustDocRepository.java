package ngvgroup.com.crm.features.customer.repository.inf;

import java.util.Optional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.inf.CrmInfCustDoc;

import org.springframework.stereotype.Repository;

@Repository
public interface CrmInfCustDocRepository extends BaseRepository<CrmInfCustDoc> {

    Optional<CrmInfCustDoc> findByCustomerCode(String customerCode);
}
