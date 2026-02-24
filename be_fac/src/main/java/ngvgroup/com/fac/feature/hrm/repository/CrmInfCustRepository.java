package ngvgroup.com.fac.feature.hrm.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.fac.feature.hrm.dto.ObjectTxnDto;
import ngvgroup.com.fac.feature.hrm.model.CrmInfCust;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CrmInfCustRepository extends BaseRepository<CrmInfCust> {

    @Query("""
                select new ngvgroup.com.fac.feature.hrm.dto.ObjectTxnDto(
                    c.customerCode,
                    c.customerName,
                    d.identificationId,
                    a.address,
                    d.issueDate,
                    d.issuePlace
                    )
                from CrmInfCust c
                left join CrmInfCustDoc d on c.customerCode = d.customerCode
                left join CrmInfCustAddr a on c.customerCode = a.customerCode
                where c.isDelete = 0
            """)
    Page<ObjectTxnDto> getTxnList(Pageable pageable);

    CrmInfCust findByCustomerCode(String customerCode);

}
