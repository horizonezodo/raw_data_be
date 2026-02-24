package ngvgroup.com.crm.features.common.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_INF_ORGANIZATION")
public class ComInfOrganization extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    @Column(name = "ORG_LEGAL_CODE", length = 128)
    private String orgLegalCode;

    @Column(name = "ORG_NAME", length = 256, nullable = false)
    private String orgName;

    @Column(name = "ORG_LEGAL_NAME", length = 256)
    private String orgLegalName;

    @Column(name = "ORG_SHORT_NAME", length = 256)
    private String orgShortName;

    @Column(name = "ORG_ENGLISH_NAME", length = 256)
    private String orgEnglishName;

    @Column(name = "ORG_LEVEL", length = 128)
    private String orgLevel;

    @Column(name = "ORG_LEVEL_NUMBER")
    private Integer orgLevelNumber;

    @Column(name = "PARENT_CODE", length = 128)
    private String parentCode;

    @Column(name = "TAX_CODE", length = 64)
    private String taxCode;

    @Column(name = "REGISTRATION_NUMBER", length = 256)
    private String registrationNumber;

    @Column(name = "REGISTRATION_TYPE", length = 128)
    private String registrationType;

    @Column(name = "REGISTRATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @Column(name = "REGISTRATION_EXPIRY")
    @Temporal(TemporalType.DATE)
    private Date registrationExpiry;

    @Column(name = "ADDRESS", length = 512)
    private String address;

    @Column(name = "LEGAL_REPRESENTATIVE", length = 256)
    private String legalRepresentative;

    @Column(name = "LEGAL_REP_ID", length = 64)
    private String legalRepId;

    @Column(name = "MOBILE_NUMBER", length = 128)
    private String mobileNumber;

    @Column(name = "PROVINCE_CODE", length = 128)
    private String provinceCode;

    @Column(name = "DISTRICT_CODE", length = 128)
    private String districtCode;

    @Column(name = "WARD_CODE", length = 128)
    private String wardCode;

    @Column(name = "EMAIL", length = 256)
    private String email;

    @Column(name = "PHONE_NUMBER", length = 128)
    private String phoneNumber;

    @Column(name = "WEBSITE", length = 128)
    private String website;

    @Column(name = "BANK_ACCOUNT", length = 64)
    private String bankAccount;

    @Column(name = "BANK_NAME", length = 256)
    private String bankName;

    @Column(name = "FAX", length = 32)
    private String fax;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
