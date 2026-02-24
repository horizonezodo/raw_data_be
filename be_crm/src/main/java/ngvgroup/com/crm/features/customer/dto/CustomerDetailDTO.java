package ngvgroup.com.crm.features.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailDTO {

    // --- General Info (CUST) --- (15 fields)
    private Date txnDate;
    private String processInstanceCode;
    private String customerCode;
    private String customerName;
    private String customerType;
    private String orgCode;
    private String areaCode;
    private String mobileNumber;
    private String phoneNumber;
    private String email;
    private String taxCode;
    private String fax;
    private String economicTypeCode;
    private String industryCode;
    private Integer isInsurance;

    // --- Individual Info (INDV) --- (8 fields)
    private String genderCode;
    private Date dateOfBirth;
    private String placeOfBirth;
    private String ethnicityCode;
    private String maritalStatus;
    private String professionTypeCode;
    private String eduLevelCode;
    private String eduBackgroundCode;

    // --- Document Info (DOC) --- (6 fields)
    private String identificationType;
    private String identificationId;
    private String identificationIdOld;
    private Date issueDate;
    private Date expiryDate;
    private String issuePlace;

    // --- Corporate Info (CORP) --- (10 fields)
    private String corpShortName;
    private String businessLicenseNo;
    private Date businessLicenseDate;
    private String issuedBy;
    private Date establishedDate;
    private String website;
    private String legalRepName;
    private String legalRepTitle;
    private String legalRepIdNo;
    private String industryDetail;

    // --- Address Info (ADDR) --- (4 fields)
    private String provinceCode;
    private String wardCode;
    private String address;
    private Integer isPrimary;

    // --- Extension Info (SEG + INDV) --- (13 fields)
    private String poorHouseholdBookNo;
    private Integer isPoorHousehold;
    private String segmentType;
    private String segmentCode;
    private String segmentRankCode;
    private String profession;
    private String jobTitle;
    private String workTimeValue;
    private String workTimeUnit;
    private String contractType;
    private String workplace;
    private String workAddress;
    private String description;
}
