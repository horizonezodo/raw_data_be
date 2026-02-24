package ngvgroup.com.loan.feature.interest_rate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgInterestRateDtlDto {
    private Long id;
    private String interestCode;
    private String orgCode;
    private Integer isActive;
    private BigDecimal interestRate;
    private BigDecimal negotiatedRate;
    private BigDecimal previousRate;

    private Date effectiveDate;
    private BigInteger amountFrom;
    private BigInteger amountTo;
    private String docNo;

    private Date docEffectiveDate;
}

