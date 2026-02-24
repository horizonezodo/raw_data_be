package ngvgroup.com.fac.feature.single_entry_acct.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionInfoDTO {
    private TxnAcctEntryDTOForm acctEntryForm;
    private TxnAcctDTOForm acctForm;
}
