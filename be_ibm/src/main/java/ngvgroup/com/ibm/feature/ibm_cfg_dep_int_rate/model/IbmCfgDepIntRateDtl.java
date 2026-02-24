package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "IBM_CFG_DEP_INT_RATE_DTL")
public class IbmCfgDepIntRateDtl extends BaseEntity  {

    @Column(name = "ORG_CODE", length = 64, nullable = false, columnDefinition = "default '%'")
    private String orgCode;

    @Column(name = "INTEREST_RATE_CODE", length = 64, nullable = false)
    private String interestRateCode;

    @Column(name = "EFFECTIVE_DATE", nullable = false)
    private Timestamp effectiveDate;

    @Column(name = "INTEREST_RATE", precision = 7, scale = 5)
    private BigDecimal interestRate;

    @Column(name = "INTEREST_RATE_MIN", precision = 7, scale = 5)
    private BigDecimal interestRateMin;

    @Column(name = "INTEREST_RATE_MAX", precision = 7, scale = 5)
    private BigDecimal interestRateMax;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
}
