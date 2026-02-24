package ngvgroup.com.loan.feature.interest_process.model.cfg;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.loan.feature.interest_process.projection.FixedRateEntity;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "LNM_CFG_INT_RATE_FIXED")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LnmCfgIntRateFixed extends BaseEntity implements FixedRateEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "INTEREST_RATE_CODE", nullable = false, length = 64)
    private String interestRateCode;

    @Column(name = "EFFECTIVE_DATE", nullable = false)
    private Date effectiveDate;

    @Column(name = "DECISION_NO", length = 128)
    private String decisionNo;

    @Column(name = "DECISION_DATE")
    private Date decisionDate;

    @Column(name = "INTEREST_RATE", precision = 9, scale = 5)
    private BigDecimal interestRate;

    @Column(name = "INTEREST_RATE_MIN", precision = 9, scale = 5)
    private BigDecimal interestRateMin;

    @Column(name = "INTEREST_RATE_MAX", precision = 9, scale = 5)
    private BigDecimal interestRateMax;

    @Column(name = "BASE_RATE_CODE", length = 64)
    private String baseRateCode;

    @Column(name = "RESET_FREQ", length = 64)
    private String resetFreq;

    @Column(name = "SPREAD", precision = 9, scale = 5)
    private BigDecimal spread;

    @Column(name = "FLOOR_INTEREST_RATE", precision = 9, scale = 5)
    private BigDecimal floorInterestRate;

    @Column(name = "CAP_INTEREST_RATE", precision = 9, scale = 5)
    private BigDecimal capInterestRate;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
