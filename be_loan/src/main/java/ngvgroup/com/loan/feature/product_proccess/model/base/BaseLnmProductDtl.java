package ngvgroup.com.loan.feature.product_proccess.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseLnmProductDtl extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    protected String orgCode;

    @Column(name = "EFFECTIVE_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    protected Date effectiveDate;

    @Column(name = "LNM_PRODUCT_CODE", nullable = false, length = 64)
    protected String lnmProductCode;

    @Column(name = "MATURITY_DATE_RULE", length = 128)
    protected String maturityDateRule;

    @Column(name = "INTEREST_PAYMENT_METHOD", length = 64)
    protected String interestPaymentMethod;

    @Column(name = "IS_ACCRUED_CALC", precision = 1)
    protected Integer isAccruedCalc;

    @Column(name = "PRINCIPAL_GRACE_DAY", precision = 4)
    protected Integer principalGraceDay;

    @Column(name = "INTEREST_GRACE_DAY", precision = 4)
    protected Integer interestGraceDay;

    @Column(name = "INTEREST_CALC_METHOD", length = 128)
    protected String interestCalcMethod;

    @Column(name = "INTEREST_BASE_CODE", length = 128)
    protected String interestBaseCode;

    @Column(name = "INTEREST_DATE_METHOD", length = 128)
    protected String interestDateMethod;

    @Column(name = "INTEREST_START_METHOD", length = 128)
    protected String interestStartMethod;

    @Column(name = "IS_INTEREST_DIFF_MGMT", precision = 1)
    protected Integer isInterestDiffMgmt;

    @Column(name = "OVERDUE_CHECK_COND", length = 128)
    protected String overdueCheckCond;

    @Column(name = "OVERDUE_INTEREST_TYPE", length = 128)
    protected String overdueInterestType;

    @Column(name = "OVERDUE_INTEREST_VALUE", precision = 7, scale = 4)
    protected BigDecimal overdueInterestValue;

    @Column(name = "PENALTY_INTEREST_TYPE", length = 128)
    protected String penaltyInterestType;

    @Column(name = "PENALTY_INTEREST_VALUE", precision = 7, scale = 4)
    protected BigDecimal penaltyInterestValue;

    @Column(name = "RECORD_STATUS", length = 64)
    protected String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    protected Integer isActive = 1;
}

