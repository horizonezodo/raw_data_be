package ngvgroup.com.crm.features.customer.repository.history;

import java.util.List;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.history.CrmInfCustRelnA;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CrmInfCustRelnARepository extends BaseRepository<CrmInfCustRelnA> {

    @Modifying
    @Query("UPDATE CrmInfCustRelnA h SET h.cifNumber = :cif WHERE h.customerCode = :code")
    void updateCifByCustomerCode(@Param("code") String customerCode, @Param("cif") String cifNumber);

    List<CrmInfCustRelnA> findAllByCustomerCode(String customerCode);
}
