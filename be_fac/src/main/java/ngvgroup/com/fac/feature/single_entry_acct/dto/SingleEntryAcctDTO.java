package ngvgroup.com.fac.feature.single_entry_acct.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleEntryAcctDTO {
    private TransactionInfoDTO transactionInfo;
    private List<AccountingInfoDTO> accountingInfo;
}