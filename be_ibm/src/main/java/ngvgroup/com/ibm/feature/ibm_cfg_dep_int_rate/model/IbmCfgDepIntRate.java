package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "IBM_CFG_DEP_INT_RATE")
public class IbmCfgDepIntRate extends BaseEntity {

    @Column(name = "INTEREST_RATE_CODE", length = 64, nullable = true)
    private String interestRateCode;

    @Column(name = "ORG_CODE", length = 64, nullable = true, columnDefinition = "default '%'")
    private String orgCode;

    @Column(name = "CURRENCY_CODE", length = 4, nullable = false)
    private String currencyCode;

    @Column(name = "INTEREST_RATE_NAME", length = 256, nullable = false)
    private String interestRateName;

    @Column(name = "INTEREST_RATE_TYPE", length = 64)
    private String interestRateType;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
}
