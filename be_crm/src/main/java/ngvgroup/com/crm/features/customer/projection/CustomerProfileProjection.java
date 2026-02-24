package ngvgroup.com.crm.features.customer.projection;

import java.util.Date;

public interface CustomerProfileProjection {
    Date getTxnDate();

    String getProcessInstanceCode();

    String getCustomerCode();

    String getCustomerName();

    String getCustomerType(); // Mapping tên loại KH

    String getOrgCode(); // Hiển thị mã - tên

    String getAreaCode(); // Hiển thị tên khu vực

    String getMobileNumber();

    String getPhoneNumber();

    String getEmail();

    String getTaxCode();

    String getFax();

    String getEconomicTypeCode(); // Mapping tên

    String getIndustryCode(); // Mapping tên

    Integer getIsInsurance();

    // Cá nhân
    String getGenderCode(); // Mapping tên

    Date getDateOfBirth();

    String getPlaceOfBirth();

    String getEthnicityCode(); // Mapping tên

    String getMaritalStatus(); // Mapping tên

    String getProfessionTypeCode(); // Mapping tên

    String getEduLevelCode(); // Mapping tên

    String getEduBackgroundCode(); // Mapping tên

    String getIdentificationType(); // Mapping tên

    String getIdentificationId();

    String getIdentificationIdOld();

    Date getIssueDate();

    Date getExpiryDate();

    String getIssuePlace();

    // Doanh nghiệp
    String getCorpShortName();

    String getBusinessLicenseNo();

    Date getBusinessLicenseDate();

    String getIssuedBy();

    Date getEstablishedDate();

    String getWebsite();

    String getLegalRepName();

    String getLegalRepTitle();

    String getLegalRepIdNo();

    String getIndustryDetail();

    // Địa chỉ
    String getProvinceCode(); // Mapping tên

    String getWardCode(); // Mapping tên

    String getAddress();

    Integer getIsPrimary();

    // Thông tin mở rộng
    String getPoorHouseholdBookNo();

    Integer getIsPoorHousehold();

    String getSegmentType(); // Mapping tên

    String getSegmentCode(); // Mapping tên

    String getSegmentRankCode(); // Mapping tên

    String getProfession();

    String getJobTitle();

    // Logic thời gian công tác ghép năm/tháng
    String getWorkTimeValue();

    String getWorkTimeUnit();

    String getContractType();

    String getWorkplace();

    String getWorkAddress();

    String getDescription();
}