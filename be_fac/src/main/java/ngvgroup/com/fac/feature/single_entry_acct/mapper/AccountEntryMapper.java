package ngvgroup.com.fac.feature.single_entry_acct.mapper;

import ngvgroup.com.fac.feature.single_entry_acct.dto.*;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcct;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntry;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntryDtl;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountEntryMapper {
    FacTxnAcctEntry toFacCfgAcctEntry(TxnAcctEntryDTOForm entryDTOForm);
    FacTxnAcct toFacCfgAcct(TxnAcctDTOForm acctDTOForm);
    FacTxnAcctEntryDtl toFacTxnAcctEntryDtl(AccountingInfoDTO dto);
}
