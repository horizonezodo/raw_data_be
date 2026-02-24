package ngvgroup.com.bpmn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "CRM_INF_CUSTOMER")
public class CrmInfCustomer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "BUSINESS_STATUS", length = 32, nullable = false)
    private String businessStatus;

    @Column(name = "CIF_NUMBER", length = 64)
    private String cifNumber;

    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    @Column(name = "CUSTOMER_CODE", length = 128, nullable = false)
    private String customerCode;

    @Column(name = "CUSTOMER_NAME", length = 256)
    private String customerName;

    @Column(name = "CUSTOMER_TYPE", length = 32)
    private String customerType;

    @Column(name = "AREA_CODE", length = 128)
    private String areaCode;

    @Column(name = "CURRENT_ADDRESS", length = 512)
    private String currentAddress;

    @Column(name = "PERMANENT_ADDRESS", length = 512)
    private String permanentAddress;

    @Column(name = "MOBILE_NUMBER", length = 128)
    private String mobileNumber;

    @Column(name = "PHONE_NUMBER", length = 128)
    private String phoneNumber;

    @Column(name = "EMAIL", length = 256)
    private String email;

    @Column(name = "TAX_CODE", length = 64)
    private String taxCode;

    @Column(name = "FAX", length = 32)
    private String fax;

    @Column(name = "BANK_ACCOUNT", length = 64)
    private String bankAccount;

    @Column(name = "BANK_NAME", length = 256)
    private String bankName;

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Column(name = "PLACE_OF_BIRTH", length = 128)
    private String placeOfBirth;

    @Column(name = "GENDER_CODE", length = 16)
    private String genderCode;

    @Column(name = "IDENTIFICATION_TYPE", length = 32)
    private String identificationType;

    @Column(name = "IDENTIFICATION_ID", length = 128)
    private String identificationId;

    @Column(name = "IDENTIFICATION_ID_OLD", length = 16)
    private String identificationIdOld;

    @Column(name = "ISSUE_DATE")
    private Date issueDate;

    @Column(name = "ISSUE_PLACE", length = 256)
    private String issuePlace;

    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;

    @Column(name = "ETHNICITY", length = 32)
    private String ethnicity;

    @Column(name = "PROFESSION_TYPE", length = 64)
    private String professionType;

    @Column(name = "EDUCATION_BACKGROUND", length = 32)
    private String educationBackground;

    @Column(name = "EDUCATION_LEVEL", length = 32)
    private String educationLevel;

    @Column(name = "MARITAL_STATUS", length = 64)
    private String maritalStatus;

    @Column(name = "HOUSEHOLD_NUMBER", length = 64)
    private String householdNumber;

    @Column(name = "HOUSEHOLD_ISSUE_PLACE", length = 256)
    private String householdIssuePlace;

    @Column(name = "HOUSEHOLD_ISSUE_DATE")
    private Date householdIssueDate;

    @Column(name = "IS_INSURANCE")
    private Boolean isInsurance;

    @Column(name = "IS_POOR_HOUSEHOLD")
    private Boolean isPoorHousehold;

    @Column(name = "POOR_HOUSEHOLD_BOOK", length = 64)
    private String poorHouseholdBook;

    @Column(name = "IS_EMPLOYEE")
    private Boolean isEmployee;

    @Column(name = "IS_ARTICLE_127")
    private Boolean isArticle127;

    @Column(name = "POSITION_CODE", length = 32)
    private String positionCode;

    @Column(name = "CUSTOMER_SEGMENT", length = 64)
    private String customerSegment;

    @Column(name = "CUSTOMER_RANK", length = 64)
    private String customerRank;

    @Column(name = "LOYALTY_CARD_NUMBER", length = 128)
    private String loyaltyCardNumber;

    @Column(name = "PROFESSION", length = 256)
    private String profession;

    @Column(name = "WORKPLACE", length = 128)
    private String workplace;

    @Column(name = "WORK_ADDRESS", length = 256)
    private String workAddress;

    @Column(name = "JOB_TITLE", length = 64)
    private String jobTitle;

    @Column(name = "WORK_DURATION_YEARS")
    private Integer workDurationYears;

    @Column(name = "WORK_DURATION_MONTHS")
    private Integer workDurationMonths;

    @Column(name = "CONTRACT_TYPE", length = 64)
    private String contractType;

    @Column(name = "ECONOMIC_TYPE_CODE", length = 64)
    private String economicTypeCode;

    @Column(name = "INDUSTRY_CODE", length = 64)
    private String industryCode;

    @Column(name = "BENEFICIARY_NAME", length = 256)
    private String beneficiaryName;

    @Column(name = "BENEFICIARY_ACCOUNT", length = 64)
    private String beneficiaryAccount;

    @Column(name = "BENEFICIARY_BANK", length = 256)
    private String beneficiaryBank;
}
