package ngvgroup.com.fac.feature.single_entry_acct.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FAC_TXN_ACCT_ENTRY_DTL")
public class FacTxnAcctEntryDtl extends BaseEntity {

    @Column(name = "BUSINESS_STATUS", nullable = false, length = 32)
    private String businessStatus;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "TXN_DATE", nullable = false)
    private LocalDate txnDate;

    @Column(name = "TXN_ACCT_ENTRY_DTL_CODE", nullable = false, length = 32)
    private String txnAcctEntryDtlCode;

    @Column(name = "REFERENCE_CODE", length = 128)
    private String referenceCode;

    @Column(name = "PROCESS_INSTANCE_CODE", nullable = false, length = 128)
    private String processInstanceCode;

    @Column(name = "TXN_ACCT_ENTRY_CODE", nullable = false, length = 128)
    private String txnAcctEntryCode;

    @Column(name = "ENTRY_TYPE", nullable = false, length = 64)
    private String entryType;

    @Column(name = "ACC_NO", nullable = false, length = 128)
    private String accNo;

    @Column(name = "ACC_CLASS_CODE", nullable = false, length = 128)
    private String accClassCode;

    @Column(name = "ACC_COA_CODE", nullable = false, length = 64)
    private String accCoaCode;

    @Column(name = "CURRENCY_CODE", nullable = false, length = 4)
    private String currencyCode;

    @Column(name = "EXCHANGE_RATE", length = 22)
    private BigDecimal exchangeRate;

    @Column(name = "LINE_FOREIGN_AMT", nullable = false, length = 22)
    private BigDecimal lineForeignAmt;

    @Column(name = "LINE_BASE_AMT", nullable = false, length = 22)
    private BigDecimal lineBaseAmt;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
