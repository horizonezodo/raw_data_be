package ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct_entry;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacTxnAcctEntryDTO {

    private String entryTypeCode;

    @NotBlank(message = "Loại phiếu không được để trống!")
    private String voucherTypeCode;

    @NotBlank(message = "Số tiền giao dịch không được để trống!")
    private BigDecimal entryForeignAmt;

    @NotBlank
    private String processInstanceCode;

    @NotBlank
    private String orgCode;

    @NotBlank
    private LocalDate txnDate;

    private String referenceCode;

    private String txnAcctEntryCode;

    private String voucherNo;
    @NotBlank
    private String businessStatus;

    @NotBlank
    private String currencyCode;
    @NotBlank
    private String entryContent;


}
