package ngvgroup.com.loan.feature.type_of_capital_use.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LnmCfgCapitalUseRateDTO {
    private Long id;
    private Timestamp effectiveDate;
    private String capitalUseCode;
    private String rateValueType;
    private String useLevel;
    private BigDecimal rateValue;
    private BigDecimal payoutRatio;
    private String orgCode;
}
