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
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FAC_TXN_ACCT")
public class FacTxnAcct extends BaseEntity {

    @Column(name = "BUSINESS_STATUS", nullable = false, length = 32)
    private String businessStatus;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "TXN_DATE", nullable = false)
    private LocalDate txnDate;

    @Column(name = "TXN_TIME", nullable = false)
    private LocalDateTime txnTime;

    @Column(name = "PROCESS_TYPE_CODE", nullable = false, length = 128)
    private String processTypeCode;

    @Column(name = "PROCESS_INSTANCE_CODE", nullable = false, length = 128)
    private String processInstanceCode;

    @Column(name = "TXN_CONTENT", nullable = false, length = 4000)
    private String txnContent;

    @Column(name = "REFERENCE_CODE", length = 128)
    private String referenceCode;

    @Column(name = "OBJECT_TYPE_CODE", length = 64)
    private String objectTypeCode;

    @Column(name = "OBJECT_TXN_CODE", length = 64)
    private String objectTxnCode;

    @Column(name = "OBJECT_TXN_NAME", length = 128)
    private String objectTxnName;

    @Column(name = "ADDRESS", length = 512)
    private String address;

    @Column(name = "IDENTIFICATION_ID", length = 128)
    private String identificationId;

    @Column(name = "ISSUE_DATE")
    private LocalDate issueDate;

    @Column(name = "ISSUE_PLACE", length = 256)
    private String issuePlace;

    @Column(name = "CURRENCY_CODE", nullable = false, length = 4)
    private String currencyCode;

    @Column(name = "TOTAL_FOREIGN_AMT", nullable = false, length = 22)
    private BigDecimal totalForeignAmt = BigDecimal.ZERO;

    @Column(name = "TOTAL_BASE_AMT", nullable = false, length = 22)
    private BigDecimal totalBaseAmt = BigDecimal.ZERO;

    @Column(name = "TOTAL_TAX_AMT", length = 22)
    private BigDecimal totalTaxAmt = BigDecimal.ZERO;

    @Column(name = "TOTAL_FEE_AMT", length = 22)
    private BigDecimal totalFeeAmt;

    @Column(name = "TOTAL_ADJUST_AMT", length = 22)
    private BigDecimal totalAdjustAmt = BigDecimal.ZERO;

    @Column(name = "TOTAL_REVERSED_AMT", length = 22)
    private BigDecimal totalReversedAmt = BigDecimal.ZERO;

    @Column(name = "RECORD_STATUS", nullable = false, length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private Integer isActive = 1;
}
