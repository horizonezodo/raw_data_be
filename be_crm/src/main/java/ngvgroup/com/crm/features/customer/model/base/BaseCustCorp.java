package ngvgroup.com.crm.features.customer.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseCustCorp extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "CUSTOMER_CODE", length = 128, nullable = false)
    private String customerCode;

    @Column(name = "CORP_NAME", length = 256)
    private String corpName;

    @Column(name = "CORP_SHORT_NAME", length = 128)
    private String corpShortName;

    @Column(name = "LEGAL_REP_NAME", length = 128)
    private String legalRepName;

    @Column(name = "LEGAL_REP_TITLE", length = 128)
    private String legalRepTitle;

    @Column(name = "LEGAL_REP_ID_NO", length = 128)
    private String legalRepIdNo;

    @Column(name = "BUSINESS_LICENSE_NO", length = 128)
    private String businessLicenseNo;

    @Column(name = "BUSINESS_LICENSE_DATE")
    private Date businessLicenseDate;

    @Column(name = "ISSUED_BY", length = 512)
    private String issuedBy;

    @Column(name = "ESTABLISHED_DATE")
    private Date establishedDate;

    @Column(name = "INDUSTRY_DETAIL", length = 512)
    private String industryDetail;

    @Column(name = "WEBSITE", length = 128)
    private String website;

    @Column(name = "REMARK", length = 256)
    private String remark;
}