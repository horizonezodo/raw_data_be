package ngvgroup.com.loan.feature.product_proccess.model.base;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseLnmProduct extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    protected String orgCode;

    @Column(name = "CURRENCY_CODE", length = 4)
    protected String currencyCode;

    @Column(name = "LNM_PRODUCT_CODE", length = 64)
    protected String lnmProductCode;

    @Column(name = "LNM_PRODUCT_NAME", length = 256)
    protected String lnmProductName;

    @Column(name = "LNM_PRODUCT_TYPE_CODE", length = 64)
    protected String lnmProductTypeCode;

    @Column(name = "LOAN_TERM_TYPE_CODE", length = 128)
    protected String loanTermTypeCode;

    @Column(name = "ACC_CLASS_CODE", length = 128)
    protected String accClassCode;

    @Column(name = "ACC_STRUCTURE_CODE", length = 64)
    protected String accStructureCode;

    @Column(name = "INTEREST_RATE_CODE", length = 64)
    protected String interestRateCode;

    @Temporal(TemporalType.DATE)
    @Column(name = "EFFECTIVE_DATE")
    protected Date effectiveDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "EXPIRY_DATE")
    protected Date expiryDate;

    @Column(name = "LOAN_TERM_FROM", precision = 4)
    protected Integer loanTermFrom;

    @Column(name = "LOAN_TERM_TO", precision = 4)
    protected Integer loanTermTo;

    @Column(name = "RECORD_STATUS", length = 64)
    protected String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    protected Integer isActive = 1;
}

