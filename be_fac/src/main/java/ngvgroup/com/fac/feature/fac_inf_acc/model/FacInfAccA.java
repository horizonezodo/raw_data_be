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
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FAC_INF_ACC_A")
public class FacInfAccA extends BaseEntity {
    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "DATA_TIME", nullable = false)
    private Timestamp dataTime;

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

    @Column(name = "ACC_PURPOSE_CODE", nullable = false, length = 128)
    private String accPurposeCode;

    @Column(name = "DOMAIN_CODE", length = 128)
    private String domainCode;

    @Column(name = "ACC_SCOPE", length = 16)
    private String accScope;

    @Column(name = "ACC_NO", nullable = false, length = 128)
    private String accNo;

    @Column(name = "ACC_CLASS_CODE", nullable = false, length = 128)
    private String accClassCode;

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

    @Column(name = "BAL", nullable = false)
    private BigDecimal bal;

    @Column(name = "BAL_AVAILABLE")
    private BigDecimal balAvailable;

    @Column(name = "BAL_ACTUAL")
    private BigDecimal balActual;

    @Column(name = "INT_ACR_PERIOD_AMT")
    private BigDecimal intAcrPeriodAmt;

    @Column(name = "INT_ACR_AMT")
    private BigDecimal intAcrAmt;

    @Column(name = "INT_ACR_YPREV_AMT")
    private BigDecimal intAcrYprevAmt;

    @Column(name = "INT_ACR_DATE")
    private Date intAcrDate;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;


    public static FacInfAccA from(FacInfAcc acc) {
        FacInfAccA a = new FacInfAccA();
        a.setDataTime(Timestamp.from(Instant.now()));
        a.setOrgCode(acc.getOrgCode());
        a.setAccClassCode(acc.getAccClassCode());
        a.setAccNature(acc.getAccNature());
        a.setAccNo(acc.getAccNo());
        a.setAccName(acc.getAccName());
        a.setAccType(acc.getAccType());
        a.setAccScope(acc.getAccScope());
        a.setCurrencyCode(acc.getCurrencyCode());
        a.setAccStatus(acc.getAccStatus());
        a.setAccPurposeCode(acc.getAccPurposeCode());
        a.setDomainCode(acc.getDomainCode());
        a.setObjectTypeCode(acc.getObjectTypeCode());
        a.setBal(acc.getBal());
        a.setIsPrimaryAccount(acc.getIsPrimaryAccount());
        a.setOpenDate(acc.getOpenDate());
        a.setBalAvailable(acc.getBalAvailable());
        a.setBalActual(acc.getBalActual());
        a.setIntAcrPeriodAmt(acc.getIntAcrPeriodAmt());
        a.setIntAcrAmt(acc.getIntAcrAmt());
        a.setIntAcrYprevAmt(acc.getIntAcrYprevAmt());
        a.setIntAcrDate(acc.getIntAcrDate());
        return a;
    }
}
