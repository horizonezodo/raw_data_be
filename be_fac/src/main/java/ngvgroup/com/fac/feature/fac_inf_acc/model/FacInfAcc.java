package ngvgroup.com.fac.feature.fac_inf_acc.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FAC_INF_ACC")
public class FacInfAcc extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "CURRENCY_CODE", nullable = false, length = 4)
    private String currencyCode;

    @Column(name = "CUSTOMER_CODE", length = 128)
    private String customerCode;

    @Column(name = "OBJECT_TYPE_CODE", length = 64)
    private String objectTypeCode;

    @Column(name = "OBJECT_CODE", length = 64)
    private String objectCode;

    @Column(name = "ACC_TYPE", nullable = false, length = 64)
    private String accType;

    @Column(name = "ACC_CLASS_CODE", nullable = false, length = 128)
    private String accClassCode;

    @Column(name = "ACC_PURPOSE_CODE", length = 128)
    private String accPurposeCode;

    @Column(name = "DOMAIN_CODE", length = 128)
    private String domainCode;

    @Column(name = "ACC_SCOPE", length = 16)
    private String accScope;

    @Column(name = "ACC_NO", nullable = false, length = 128)
    private String accNo;

    @Column(name = "ACC_NAME", nullable = false, length = 512)
    private String accName;

    @Column(name = "OPEN_DATE", nullable = false)
    private Date openDate;

    @Column(name = "CLOSE_DATE")
    private Date closeDate;

    @Column(name = "ACC_NATURE", nullable = false, length = 8)
    private String accNature;

    @Column(name = "ACC_STATUS", nullable = false, length = 16)
    private String accStatus;

    @Column(name = "IS_PRIMARY_ACCOUNT", nullable = false)
    private Integer isPrimaryAccount;

    @Column(name = "BAL", nullable = false, precision = 22)
    private BigDecimal bal = BigDecimal.ZERO;

    @Column(name = "BAL_AVAILABLE", precision = 22)
    private BigDecimal balAvailable = BigDecimal.ZERO;

    @Column(name = "BAL_ACTUAL", precision = 22)
    private BigDecimal balActual = BigDecimal.ZERO;

    @Column(name = "INT_ACR_PERIOD_AMT", precision = 22)
    private BigDecimal intAcrPeriodAmt = BigDecimal.ZERO;

    @Column(name = "INT_ACR_AMT", precision = 22)
    private BigDecimal intAcrAmt = BigDecimal.ZERO;

    @Column(name = "INT_ACR_YPREV_AMT", precision = 22)
    private BigDecimal intAcrYprevAmt = BigDecimal.ZERO;

    @Column(name = "INT_ACR_DATE")
    private Date intAcrDate;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
