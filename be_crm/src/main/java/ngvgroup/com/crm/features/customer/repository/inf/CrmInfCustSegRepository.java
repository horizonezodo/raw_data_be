package ngvgroup.com.crm.features.customer.repository.inf;

import java.util.Optional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.inf.CrmInfCustSeg;

import org.springframework.stereotype.Repository;

@Repository
public interface CrmInfCustSegRepository extends BaseRepository<CrmInfCustSeg> {

    Optional<CrmInfCustSeg> findByCustomerCode(String customerCode);
}
