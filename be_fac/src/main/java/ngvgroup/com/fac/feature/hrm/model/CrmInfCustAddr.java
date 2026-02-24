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
@Table(name = "CRM_INF_CUST_ADDR")
@Immutable
public class CrmInfCustAddr extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "CUSTOMER_CODE", nullable = false, length = 128)
    private String customerCode;

    @Column(name = "COUNTRY_CODE", nullable = false, length = 128)
    private String countryCode;

    @Column(name = "PROVINCE_CODE", length = 128)
    private String provinceCode;

    @Column(name = "WARD_CODE", length = 128)
    private String wardCode;

    @Column(name = "ADDRESS", length = 512)
    private String address;

    @Column(name = "IS_PRIMARY")
    private Integer isPrimary;
}
