package ngvgroup.com.loan.feature.interest_rate.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgInterestRateDtlDetailDto {
    private Long id;
    private BigDecimal interestRate;
    private BigDecimal negotiatedRate;
    private BigDecimal previousRate;

    private Date effectiveDate;
    private BigInteger amountFrom;
    private BigInteger amountTo;
    private String docNo;

    private Date docEffectiveDate;
}

