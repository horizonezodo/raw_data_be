package ngvgroup.com.loan.feature.interest_process.projection;

import java.math.BigDecimal;

public interface TierRateEntity extends FixedRateEntity {
    void setAmtFrom(BigDecimal amtFrom);
    void setAmtTo(BigDecimal amtTo);
}
