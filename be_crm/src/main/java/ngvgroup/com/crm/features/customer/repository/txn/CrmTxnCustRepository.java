package ngvgroup.com.crm.features.customer.repository.txn;

import java.util.List;
import java.util.Optional;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.customer.model.txn.CrmTxnCust;
import ngvgroup.com.crm.features.customer.projection.CustomerProfileProjection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CrmTxnCustRepository extends BaseRepository<CrmTxnCust> {

        @Modifying
        @Query("UPDATE CrmTxnCust h SET h.cifNumber = :cif WHERE h.customerCode = :code")
        void updateCifByCustomerCode(@Param("code") String customerCode, @Param("cif") String cifNumber);

        @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
                        "FROM CrmTxnCust c " +
                        "WHERE c.mobileNumber = :mobileNumber " +
                        "AND c.businessStatus NOT IN (:businessStatus) " +
                        "AND c.customerCode NOT IN (:customerCode)")
        boolean existsByMobileNumberAndBusinessStatus(@Param("mobileNumber") String mobileNumber,
                        @Param("businessStatus") String businessStatus,
                        @Param("customerCode") String customerCode);

        @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
                        "FROM CrmTxnCust c " +
                        "JOIN CrmTxnCustDoc cd ON c.customerCode = cd.customerCode " +
                        "WHERE cd.identificationId = :identificationId " +
                        "AND c.businessStatus NOT IN (:businessStatus) " +
                        "AND c.customerCode NOT IN (:customerCode)")
        boolean existsByIdentificationIdAndBusinessStatus(@Param("identificationId") String identificationId,
                        @Param("businessStatus") String businessStatus,
                        @Param("customerCode") String customerCode);

        Optional<CrmTxnCust> findByProcessInstanceCode(String processInstanceCode);

        @Query("""
                            SELECT c FROM CrmTxnCust c
                            WHERE c.customerCode = :customerCode
                            AND c.businessStatus NOT IN (:businessStatus)
                        """)
        Optional<CrmTxnCust> findByCustomerCode(@Param("customerCode") String customerCode,
                        @Param("businessStatus") List<String> businessStatus);

        @Query(value = """
                            SELECT
                                c.TXN_DATE as txnDate,
                                c.PROCESS_INSTANCE_CODE as processInstanceCode,
                                c.CUSTOMER_CODE as customerCode,
                                c.CUSTOMER_NAME as customerName,
                                c.CUSTOMER_TYPE as customerType,        -- Lấy code trực tiếp
                                c.ORG_CODE as orgCode,                  -- Lấy code trực tiếp
                                c.AREA_CODE as areaCode,                -- Lấy code trực tiếp
                                c.MOBILE_NUMBER as mobileNumber,
                                c.PHONE_NUMBER as phoneNumber,
                                c.EMAIL as email,
                                c.TAX_CODE as taxCode,
                                c.FAX as fax,
                                c.ECONOMIC_TYPE_CODE as economicTypeCode, -- Lấy code trực tiếp
                                c.INDUSTRY_CODE as industryCode,          -- Lấy code trực tiếp
                                c.IS_INSURANCE as isInsurance,

                                -- Thông tin cá nhân (Lấy code từ bảng INDV)
                                indv.GENDER_CODE as genderCode,
                                indv.DATE_OF_BIRTH as dateOfBirth,
                                indv.PLACE_OF_BIRTH as placeOfBirth,
                                indv.ETHNICITY_CODE as ethnicityCode,
                                indv.MARITAL_STATUS as maritalStatus,
                                indv.PROFESSION_TYPE_CODE as professionTypeCode,
                                indv.EDU_LEVEL_CODE as eduLevelCode,
                                indv.EDU_BACKGROUND_CODE as eduBackgroundCode,

                                -- Giấy tờ định danh
                                doc.IDENTIFICATION_TYPE as identificationType, -- Lấy code
                                doc.IDENTIFICATION_ID as identificationId,
                                doc.IDENTIFICATION_ID_OLD as identificationIdOld,
                                doc.ISSUE_DATE as issueDate,
                                doc.EXPIRY_DATE as expiryDate,
                                doc.ISSUE_PLACE as issuePlace,

                                -- Thông tin doanh nghiệp
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

                                -- Địa chỉ (Lấy code từ bảng ADDR)
                                addr.PROVINCE_CODE as provinceCode,
                                addr.WARD_CODE as wardCode,
                                addr.ADDRESS as address,
                                addr.IS_PRIMARY as isPrimary,

                                -- Thông tin mở rộng
                                indv.POOR_HOUSEHOLD_BOOK_NO as poorHouseholdBookNo,
                                indv.IS_POOR_HOUSEHOLD as isPoorHousehold,
                                seg.SEGMENT_TYPE as segmentType,       -- Lấy code
                                seg.SEGMENT_CODE as segmentCode,       -- Lấy code
                                seg.SEGMENT_RANK_CODE as segmentRankCode, -- Lấy code
                                indv.PROFESSION as profession,
                                indv.JOB_TITLE as jobTitle,

                                -- Logic Work Time (Giữ nguyên logic tính toán)
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

                            FROM CRM_TXN_CUST c

                            -- Chỉ giữ lại các bảng Transaction chứa dữ liệu chi tiết
                            LEFT JOIN CRM_TXN_CUST_INDV indv ON c.PROCESS_INSTANCE_CODE = indv.PROCESS_INSTANCE_CODE
                            LEFT JOIN CRM_TXN_CUST_CORP corp ON c.PROCESS_INSTANCE_CODE = corp.PROCESS_INSTANCE_CODE
                            LEFT JOIN CRM_TXN_CUST_DOC doc ON c.PROCESS_INSTANCE_CODE = doc.PROCESS_INSTANCE_CODE
                            LEFT JOIN CRM_TXN_CUST_ADDR addr ON c.PROCESS_INSTANCE_CODE = addr.PROCESS_INSTANCE_CODE AND addr.IS_PRIMARY = 1
                            LEFT JOIN CRM_TXN_CUST_SEG seg ON c.PROCESS_INSTANCE_CODE = seg.PROCESS_INSTANCE_CODE

                            WHERE c.PROCESS_INSTANCE_CODE = :processInstanceCode
                        """, nativeQuery = true)
        Optional<CustomerProfileProjection> findProfileByProcessInstanceCode(
                        @Param("processInstanceCode") String processInstanceCode);

        @Query("""
                        SELECT c FROM CrmTxnCust c
                        WHERE c.customerCode = :customerCode
                        AND c.businessStatus NOT IN ('COMPLETE', 'CANCEL')
                            """)
        Optional<CrmTxnCust> findExistingTransaction(String customerCode);
}
