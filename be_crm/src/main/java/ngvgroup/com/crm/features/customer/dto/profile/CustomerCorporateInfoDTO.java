package ngvgroup.com.crm.features.customer.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.bpm.client.annotation.AuditField;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCorporateInfoDTO {

    @AuditField("corpShortName")
    private String corpShortName;

    @AuditField("businessLicenseNo")
    private String businessLicenseNo;

    @AuditField("businessLicenseDate")
    private Date businessLicenseDate;

    @AuditField("issuedBy")
    private String issuedBy;

    @AuditField("establishedDate")
    private Date establishedDate;

    @AuditField("website")
    private String website;

    @AuditField("legalRepName")
    private String legalRepName;

    @AuditField("legalRepTitle")
    private String legalRepTitle;

    @AuditField("legalRepIdNo")
    private String legalRepIdNo;

    @AuditField("industryDetail")
    private String industryDetail;
}
