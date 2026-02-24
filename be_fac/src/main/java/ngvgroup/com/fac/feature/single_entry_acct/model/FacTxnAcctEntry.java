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
@Table(name = "FAC_TXN_ACCT_ENTRY")
public class FacTxnAcctEntry extends BaseEntity {

    @Column(name = "BUSINESS_STATUS", nullable = false, length = 32)
    private String businessStatus;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "TXN_DATE", nullable = false)
    private LocalDate txnDate;

    @Column(name = "PROCESS_INSTANCE_CODE", nullable = false, length = 128)
    private String processInstanceCode;

    @Column(name = "TXN_ACCT_ENTRY_CODE", nullable = false, length = 32)
    private String txnAcctEntryCode;

    @Column(name = "REFERENCE_CODE", length = 128)
    private String referenceCode;

    @Column(name = "ENTRY_TYPE_CODE", length = 128)
    private String entryTypeCode;

    @Column(name = "VOUCHER_TYPE_CODE", nullable = false, length = 32)
    private String voucherTypeCode;

    @Column(name = "VOUCHER_NO", nullable = false, length = 32)
    private String voucherNo;

    @Column(name = "CURRENCY_CODE", nullable = false, length = 4)
    private String currencyCode;

    @Column(name = "ENTRY_CONTENT", length = 4000)
    private String entryContent;

    @Column(name = "ENTRY_FOREIGN_AMT", nullable = false, length = 22)
    private BigDecimal entryForeignAmt = BigDecimal.ZERO;

    @Column(name = "ENTRY_BASE_AMT", nullable = false, length = 22)
    private BigDecimal entryBaseAmt = BigDecimal.ZERO;

    @Column(name = "ENTRY_TAX_AMT", length = 22)
    private BigDecimal entryTaxAmt = BigDecimal.ZERO;

    @Column(name = "ENTRY_FEE_AMT", length = 22)
    private BigDecimal entryFeeAmt = BigDecimal.ZERO;

    @Column(name = "ENTRY_ADJUST_AMT", length = 22)
    private BigDecimal entryAdjustAmt = BigDecimal.ZERO;

    @Column(name = "ENTRY_REVERSED_AMT", length = 22)
    private BigDecimal entryReversedAmt = BigDecimal.ZERO;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
