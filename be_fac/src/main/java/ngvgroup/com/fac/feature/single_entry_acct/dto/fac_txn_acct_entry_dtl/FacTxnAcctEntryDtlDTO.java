package ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct_entry_dtl;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacTxnAcctEntryDtlDTO {
    private Long id;
    private String entryType;
    private String accNo;
    private String accClassCode;
    private BigDecimal lineForeignAmt;
    private String orgCode;
    private LocalDate txnDate;
    private String txnAcctEntryDtlCode;
    private String processInstanceCode;
    private String currencyCode;
    private BigDecimal lineBaseAmt;
    private String accCoaCode;
    private String businessStatus;
    private String txnAcctEntryCode;
    private String accName;
    private BigDecimal balAvailable;
    private BigDecimal balActual;
}
