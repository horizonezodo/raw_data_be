package ngvgroup.com.crm.features.customer.repository.inf;

import java.util.Optional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.dto.CustomerRelationDetailDTO;
import ngvgroup.com.crm.features.customer.dto.CustomerSearchResultDTO;
import ngvgroup.com.crm.features.customer.dto.CustomerSearchRequestDto;
import ngvgroup.com.crm.features.customer.model.inf.CrmInfCust;
import ngvgroup.com.crm.features.customer.projection.CustomerProfileProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CrmInfCustRepository extends BaseRepository<CrmInfCust> {

    @Modifying
    @Query("UPDATE CrmInfCust h SET h.cifNumber = :cif WHERE h.customerCode = :code")
    void updateCifByCustomerCode(@Param("code") String customerCode, @Param("cif") String cifNumber);

    Optional<CrmInfCust> findByCustomerCode(String customerCode);

    boolean existsByMobileNumber(String mobileNumber);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
            "FROM CrmInfCust c " +
            "JOIN CrmInfCustDoc cd ON c.customerCode = cd.customerCode " +
            "WHERE cd.identificationId = :identificationId")
    boolean existsByIdentificationId(String identificationId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
            "FROM CrmInfCust c " +
            "WHERE c.mobileNumber = :mobileNumber " +
            "AND c.customerCode <> :customerCode")
    boolean existsByMobileNumber(String mobileNumber, String customerCode);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
            "FROM CrmInfCust c " +
            "JOIN CrmInfCustDoc cd ON c.customerCode = cd.customerCode " +
            "WHERE cd.identificationId = :identificationId " +
            "AND c.customerCode <> :customerCode")
    boolean existsByIdentificationId(String identificationId, String customerCode);

    @Query("""
            SELECT new ngvgroup.com.crm.features.customer.dto.CustomerSearchResultDTO(
                c.id,
                c.customerCode,
                c.customerName,
                c.customerType,
                ccc.commonName,
                c.mobileNumber,
                CASE
                    WHEN c.customerType = 'CM007.001' THEN d.identificationId
                    WHEN c.customerType = 'CM007.002' THEN corp.businessLicenseNo
                    ELSE d.identificationId
                END as identityNumber,
                addr.address
            )
            FROM CrmInfCust c
            LEFT JOIN CrmInfCustDoc d ON c.customerCode = d.customerCode
            LEFT JOIN CrmInfCustCorp corp ON c.customerCode = corp.customerCode
            LEFT JOIN CrmInfCustAddr addr ON c.customerCode = addr.customerCode AND addr.isPrimary = 1
            JOIN ComCfgCommon ccc ON c.customerType = ccc.commonCode
            WHERE
                (:#{#req.orgCode} IS NULL OR c.orgCode = :#{#req.orgCode})
                AND (:#{#req.keyword} IS NULL OR
                   LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :#{#req.keyword}, '%')) OR
                   LOWER(c.customerName) LIKE LOWER(CONCAT('%', :#{#req.keyword}, '%')) OR
                   LOWER(c.mobileNumber) LIKE LOWER(CONCAT('%', :#{#req.keyword}, '%')) OR
                   LOWER(d.identificationId) LIKE LOWER(CONCAT('%', :#{#req.keyword}, '%')) OR
                   LOWER(corp.businessLicenseNo) LIKE LOWER(CONCAT('%', :#{#req.keyword}, '%')) OR
                   LOWER(addr.address) LIKE LOWER(CONCAT('%', :#{#req.keyword}, '%'))
                )
                AND (:#{#req.customerTypes} IS NULL OR c.customerType IN :#{#req.customerTypes})
                AND (:#{#req.areaCodes} IS NULL OR c.areaCode IN :#{#req.areaCodes})
            ORDER BY c.modifiedDate DESC
            """)
    Page<CustomerSearchResultDTO> searchCustomers(@Param("req") CustomerSearchRequestDto req, Pageable pageable);

    @Query("""
            SELECT new ngvgroup.com.crm.features.customer.dto.CustomerRelationDetailDTO(
                c.customerCode,
                c.customerName,
                c.mobileNumber,
                d.identificationId,
                a.address
            )
            FROM CrmInfCust c
            LEFT JOIN CrmInfCustDoc d ON c.customerCode = d.customerCode
            LEFT JOIN CrmInfCustAddr a ON c.customerCode = a.customerCode
            WHERE c.customerCode = :customerCode
                """)
    CustomerRelationDetailDTO getCustomerForRelation(@Param("customerCode") String customerCode);

    @Query("""
                SELECT new ngvgroup.com.crm.features.customer.dto.CustomerRelationDetailDTO(
                    c.customerCode,
                    c.customerName,
                    c.mobileNumber,
                    d.identificationId,
                    a.address
                )
                FROM CrmInfCust c
                LEFT JOIN CrmInfCustDoc d ON c.customerCode = d.customerCode
                LEFT JOIN CrmInfCustAddr a ON c.customerCode = a.customerCode AND a.isPrimary = 1
                WHERE :keyword IS NULL OR
                LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(c.mobileNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(d.identificationId) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(a.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<CustomerRelationDetailDTO> searchCustomersForRelation(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
                SELECT
                    c.MODIFIED_DATE as txnDate, -- INF không có TXN_DATE, lấy ngày sửa đổi
                    '' as processInstanceCode,  -- INF không có Process Instance
                    c.CUSTOMER_CODE as customerCode,
                    c.CUSTOMER_NAME as customerName,
                    c.CUSTOMER_TYPE as customerType,
                    c.ORG_CODE as orgCode,
                    c.AREA_CODE as areaCode,
                    c.MOBILE_NUMBER as mobileNumber,
                    c.PHONE_NUMBER as phoneNumber,
                    c.EMAIL as email,
                    c.TAX_CODE as taxCode,
                    c.FAX as fax,
                    c.ECONOMIC_TYPE_CODE as economicTypeCode,
                    c.INDUSTRY_CODE as industryCode,
                    c.IS_INSURANCE as isInsurance,

                    -- INDV
                    indv.GENDER_CODE as genderCode,
                    indv.DATE_OF_BIRTH as dateOfBirth,
                    indv.PLACE_OF_BIRTH as placeOfBirth,
                    indv.ETHNICITY_CODE as ethnicityCode,
                    indv.MARITAL_STATUS as maritalStatus,
                    indv.PROFESSION_TYPE_CODE as professionTypeCode,
                    indv.EDU_LEVEL_CODE as eduLevelCode,
                    indv.EDU_BACKGROUND_CODE as eduBackgroundCode,

                    -- DOC
                    doc.IDENTIFICATION_TYPE as identificationType,
                    doc.IDENTIFICATION_ID as identificationId,
                    doc.IDENTIFICATION_ID_OLD as identificationIdOld,
                    doc.ISSUE_DATE as issueDate,
                    doc.EXPIRY_DATE as expiryDate,
                    doc.ISSUE_PLACE as issuePlace,

                    -- CORP
                    corp.CORP_SHORT_NAME as corpShortName,
                    corp.BUSINESS_LICENSE_NO as businessLicenseNo,
                    corp.BUSINESS_LICENSE_DATE as businessLicenseDate,
                    corp.ISSUED_BY as issuedBy,
                    corp.ESTABLISHED_DATE as establishedDate,
                    corp.WEBSITE as website,
                    corp.LEGAL_REP_NAME as legalRepName,
                    corp.LEGAL_REP_TITLE as legalRepTitle,
                    corp.LEGAL_REP_ID_NO as legalRepIdNo,
                    corp.INDUSTRY_DETAIL as industryDetail,

                    -- ADDR (Lấy địa chỉ chính)
                    addr.PROVINCE_CODE as provinceCode,
                    addr.WARD_CODE as wardCode,
                    addr.ADDRESS as address,
                    addr.IS_PRIMARY as isPrimary,

                    -- EXTENSION (SEG + INDV columns)
                    indv.POOR_HOUSEHOLD_BOOK_NO as poorHouseholdBookNo,
                    indv.IS_POOR_HOUSEHOLD as isPoorHousehold,
                    seg.SEGMENT_TYPE as segmentType,
                    seg.SEGMENT_CODE as segmentCode,
                    seg.SEGMENT_RANK_CODE as segmentRankCode,
                    indv.PROFESSION as profession,
                    indv.JOB_TITLE as jobTitle,

                    CASE
                        WHEN indv.WORK_YEARS IS NOT NULL AND indv.WORK_YEARS <> 0 THEN indv.WORK_YEARS
                        WHEN indv.WORK_MONTHS IS NOT NULL AND indv.WORK_MONTHS <> 0 THEN indv.WORK_MONTHS
                        ELSE NULL
                    END as workTimeValue,
                    CASE
                        WHEN indv.WORK_YEARS IS NOT NULL AND indv.WORK_YEARS <> 0 THEN 'YEARS'
                        WHEN indv.WORK_MONTHS IS NOT NULL AND indv.WORK_MONTHS <> 0 THEN 'MONTHS'
                        ELSE NULL
                    END as workTimeUnit,


                    indv.CONTRACT_TYPE as contractType,
                    indv.WORKPLACE as workplace,
                    indv.WORK_ADDRESS as workAddress,
                    c.DESCRIPTION as description

                FROM CRM_INF_CUST c

                LEFT JOIN CRM_INF_CUST_INDV indv ON c.CUSTOMER_CODE = indv.CUSTOMER_CODE
                LEFT JOIN CRM_INF_CUST_CORP corp ON c.CUSTOMER_CODE = corp.CUSTOMER_CODE
                LEFT JOIN CRM_INF_CUST_DOC doc ON c.CUSTOMER_CODE = doc.CUSTOMER_CODE
                LEFT JOIN CRM_INF_CUST_ADDR addr ON c.CUSTOMER_CODE = addr.CUSTOMER_CODE AND addr.IS_PRIMARY = 1
                LEFT JOIN CRM_INF_CUST_SEG seg ON c.CUSTOMER_CODE = seg.CUSTOMER_CODE

                WHERE c.CUSTOMER_CODE = :customerCode
            """, nativeQuery = true)
    Optional<CustomerProfileProjection> findInfProfileByCustomerCode(@Param("customerCode") String customerCode);
}
