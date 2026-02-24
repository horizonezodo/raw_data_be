package ngvgroup.com.loan.feature.interest_rate.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
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

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
