package ngvgroup.com.loan.feature.interest_process.projection;

import java.math.BigDecimal;
import java.util.Date;

public interface FixedRateEntity {
    void setDecisionNo(String decisionNo);
    void setDecisionDate(Date decisionDate);
    void setEffectiveDate(Date effectiveDate);

    void setInterestRate(BigDecimal interestRate);
    void setInterestRateMin(BigDecimal interestRateMin);
    void setInterestRateMax(BigDecimal interestRateMax);
    void setFloorInterestRate(BigDecimal floorInterestRate);
    void setCapInterestRate(BigDecimal capInterestRate);
    void setResetFreq(String resetFreq);
    void setBaseRateCode(String baseRateCode);
    void setSpread(BigDecimal spread);
}
