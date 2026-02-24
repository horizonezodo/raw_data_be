package ngvgroup.com.loan.feature.type_of_capital_use.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LNM_CFG_CAPITAL_USE_RATE")
public class LnmCfgCapitalUseRate extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64 , nullable = false)
    private String orgCode;
    @Column(name = "EFFECTIVE_DATE", nullable = false)
    private Timestamp effectiveDate;
    @Column(name = "CAPITAL_USE_CODE", length = 64 , nullable = false)
    private String capitalUseCode;
    @Column(name = "RATE_VALUE_TYPE", length = 64 , nullable = false)
    private String rateValueType;
    @Column(name = "USE_LEVEL", length = 64, nullable = false)
    private String useLevel;
    @Column(name = "RATE_VALUE", length = 7 , scale = 5, nullable = false)
    private BigDecimal rateValue;
    @Column(name = "PAYOUT_RATIO", length = 7 , scale = 5, nullable = false)
    private BigDecimal payoutRatio;
}
