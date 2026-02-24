package ngvgroup.com.fac.feature.double_entry_accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct.FacTxnAcctDTO;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct_entry.FacTxnAcctEntryDTO;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct_entry_dtl.FacTxnAcctEntryDtlDTO;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoubleEntryAccountingProcessDto {

    private FacTxnAcctDTO facTxnAcctDTO;
    private FacTxnAcctEntryDTO facTxnAcctEntryDto;
    private List<FacTxnAcctEntryDtlDTO> facTxnAcctEntryDtlDtos;
    private Set<String> deletedFileIds;
}
