package com.naas.category_service.model;

import com.naas.category_service.constant.StatusConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "CTG_CFG_INTEREST_RATE_DTL")
public class CtgCfgInterestRateDtl extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name="ORG_CODE",length = 64)
    private String orgCode;

    @Column(name="INTEREST_CODE",length = 64,nullable = false)
    private String interestCode;

    @Column(name="EFFECTIVE_DATE",nullable = false)
    private Date effectiveDate;

    @Column(name="INTEREST_RATE" ,nullable = false,precision = 7,scale = 5)
    private BigDecimal interestRate;

    @Column(name="NEGOTIATED_RATE" ,nullable = false,precision = 7,scale = 5)
    private BigDecimal  negotiatedRate;

    @Column(name="PREVIOUS_RATE" ,nullable = false,precision = 7,scale = 5)
    private BigDecimal  previousRate;

    @Column(name="AMOUNT_FROM")
    private BigInteger amountFrom;

    @Column (name="AMOUNT_TO")
    private BigInteger amountTo;

    @Column(name="DOC_NO",length = 128)
    private String docNo;

    @Column(name="DOC_EFFECTIVE_DATE")
    private Date docEffectiveDate;

    public CtgCfgInterestRateDtl(String orgCode,String interestCode,Boolean isActive,BigDecimal interestRate,BigDecimal negotiatedRate,BigDecimal previousRate,Date effectiveDate,BigInteger amountFrom,BigInteger amountTo,String docNo,Date docEffectiveDate) {
        this.setApprovedBy(getCurrentUsername());
        this.setApprovedDate(getTimestampNow());
        this.setRecordStatus(StatusConstants.APPROVAL);
        this.orgCode = orgCode;
        this.interestCode = interestCode;
        this.isActive = isActive;
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
