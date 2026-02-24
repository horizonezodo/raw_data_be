package ngvgroup.com.fac.feature.hrm.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.Immutable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CRM_INF_CUST")
@Immutable
public class CrmInfCust extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "AREA_CODE", length = 128)
    private String areaCode;

    @Column(name = "CUSTOMER_CODE", length = 128)
    private String customerCode;

    @Column(name = "CUSTOMER_NAME", length = 256)
    private String customerName;

    @Column(name = "CUSTOMER_TYPE", length = 32)
    private String customerType;

    @Column(name = "TAX_CODE", length = 64)
    private String taxCode;

    @Column(name = "EMAIL", length = 256)
    private String email;

    @Column(name = "MOBILE_NUMBER", length = 128)
    private String mobileNumber;

    @Column(name = "PHONE_NUMBER", length = 128)
    private String phoneNumber;

    @Column(name = "ECONOMIC_TYPE_CODE", length = 64)
    private String economicTypeCode;

    @Column(name = "INDUSTRY_CODE", length = 64)
    private String industryCode;

    @Column(name = "IS_INSURANCE")
    private Integer isInsurance;
}
