package com.naas.category_service.dto.CtgCfgInterestRateDtl;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Boolean isActive;
    private BigDecimal interestRate;
    private BigDecimal negotiatedRate;
    private BigDecimal previousRate;

    private Date effectiveDate;
    private BigInteger amountFrom;
    private BigInteger amountTo;
    private String docNo;

    private Date docEffectiveDate;

    public CtgCfgInterestRateDtlDto( Long id,BigDecimal interestRate, BigDecimal negotiatedRate, BigDecimal previousRate, Date effectiveDate, BigInteger amountFrom, BigInteger amountTo, String docNo, Date docEffectiveDate) {
        this.id = id;
        this.interestRate = interestRate;
        this.negotiatedRate = negotiatedRate;
        this.previousRate = previousRate;
        this.effectiveDate = effectiveDate;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
        this.docNo = docNo;
        this.docEffectiveDate = docEffectiveDate;
    }
}

