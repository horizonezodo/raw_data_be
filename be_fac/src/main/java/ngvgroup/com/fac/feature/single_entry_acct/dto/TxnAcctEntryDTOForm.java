package ngvgroup.com.fac.feature.single_entry_acct.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TxnAcctEntryDTOForm {
    private CommonTransactionInfoDTO common;

    @NotBlank(message = "Loại chứng từ không được để trống!")
    private String voucherTypeCode;

    private String entryTypeCode;

    @NotBlank(message = "Số tiền giao dịch không được để trống!")
    private BigDecimal entryForeignAmt;
}
