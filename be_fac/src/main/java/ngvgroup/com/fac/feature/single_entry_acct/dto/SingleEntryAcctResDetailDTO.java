package ngvgroup.com.fac.feature.single_entry_acct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleEntryAcctResDetailDTO {
    private String voucherTypeCode;
    private String processInstanceCode;
    private String entryTypeCode;
    private BigDecimal entryForeignAmt;
    private BigDecimal totalForeignAmt;
    private LocalDate txnDate;
    private String txnContent;
    private String orgCode;
    private LocalDateTime txnTime;
    private String objectTypeCode;
    private String objectTxnCode;
    private String objectTxnName;
    private String identificationId;
    private LocalDate issueDate;
    private String issuePlace;
    private String address;
    private String accClassCode;
    private String entryType;
    private String accNo;
    private String accCoaCode;
    private BigDecimal lineForeignAmt;
    private BigDecimal lineBaseAmt;
    private Long idDtl;
    private BigDecimal balAvailable;
    private BigDecimal balActual;
}
