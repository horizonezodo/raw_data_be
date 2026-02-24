package ngvgroup.com.crm.features.customer.repository.txn;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.dto.profile.CustomerRelationDTO;
import ngvgroup.com.crm.features.customer.model.txn.CrmTxnCustReln;

@Repository
public interface CrmTxnCustRelnRepository extends BaseRepository<CrmTxnCustReln> {

        @Modifying
        @Query("UPDATE CrmTxnCustReln h SET h.cifNumber = :cif WHERE h.customerCode = :code")
        void updateCifByCustomerCode(@Param("code") String customerCode, @Param("cif") String cifNumber);

        CrmTxnCustReln findByCustomerCodeAndRelatedCustomerCodeAndProcessInstanceCode(String customerCode,
                        String relatedCustomerCode, String processInstanceCode);

        List<CrmTxnCustReln> findAllByProcessInstanceCode(String processInstanceCode);

        List<CrmTxnCustReln> findAllByCustomerCode(String customerCode);

        List<CrmTxnCustReln> findAllByCustomerCodeAndProcessInstanceCode(String customerCode,
                        String processInstanceCOde);

        @Query("""
                        SELECT new ngvgroup.com.crm.features.customer.dto.profile.CustomerRelationDTO(
                            r.relatedCustomerCode,
                            r.relationGroupCode,
                            r.relationCode,
                            r.reciprocalRelationCode,
                            r.relationStatus,
                            cust.customerName,
                            cust.mobileNumber,
                            doc.identificationId,
                            addr.address,
                            crgc.commonName,
                            crc.commonName,
                            crrc.commonName,
                            crs.commonName
                        )
                        FROM CrmTxnCustReln r
                        LEFT JOIN CrmInfCust cust ON cust.customerCode = r.relatedCustomerCode
                        LEFT JOIN CrmInfCustDoc doc ON doc.customerCode = r.relatedCustomerCode
                        LEFT JOIN CrmInfCustAddr addr ON addr.customerCode = r.relatedCustomerCode AND addr.isPrimary = 1
                        LEFT JOIN ComCfgCommon crgc ON r.relationGroupCode = crgc.commonCode
                        LEFT JOIN ComCfgCommon crc ON r.relationCode = crc.commonCode
                        LEFT JOIN ComCfgCommon crrc ON r.reciprocalRelationCode = crrc.commonCode
                        LEFT JOIN ComCfgCommon crs ON r.relationStatus = crs.commonCode
                        WHERE r.customerCode = :customerCode AND r.isDelete = 0 And r.processInstanceCode = :processInstanceCode
                        ORDER BY r.modifiedDate DESC
                        """)
        List<CustomerRelationDTO> findRelationsByCustomerCode(@Param("customerCode") String customerCode,
                        @Param("processInstanceCode") String processInstanceCode);
}
