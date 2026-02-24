package ngvgroup.com.crm.features.customer.repository.inf;

import java.util.List;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.dto.profile.CustomerRelationDTO;
import ngvgroup.com.crm.features.customer.model.inf.CrmInfCustReln;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CrmInfCustRelnRepository extends BaseRepository<CrmInfCustReln> {

    @Modifying
    @Query("UPDATE CrmInfCustReln h SET h.cifNumber = :cif WHERE h.customerCode = :code")
    void updateCifByCustomerCode(@Param("code") String customerCode, @Param("cif") String cifNumber);

    void deleteAllByCustomerCode(String customerCode);

    List<CrmInfCustReln> findAllByCustomerCode(String customerCode);

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
            FROM CrmInfCustReln r
            LEFT JOIN CrmInfCust cust ON cust.customerCode = r.relatedCustomerCode
            LEFT JOIN CrmInfCustDoc doc ON doc.customerCode = r.relatedCustomerCode
            LEFT JOIN CrmInfCustAddr addr ON addr.customerCode = r.relatedCustomerCode AND addr.isPrimary = 1
            LEFT JOIN ComCfgCommon crgc ON r.relationGroupCode = crgc.commonCode
            LEFT JOIN ComCfgCommon crc ON r.relationCode = crc.commonCode
            LEFT JOIN ComCfgCommon crrc ON r.reciprocalRelationCode = crrc.commonCode
            LEFT JOIN ComCfgCommon crs ON r.relationStatus = crs.commonCode
            WHERE r.customerCode = :customerCode AND r.isDelete = 0
            ORDER BY r.modifiedDate DESC
            """)
    List<CustomerRelationDTO> findRelationsByCustomerCode(@Param("customerCode") String customerCode);
}
