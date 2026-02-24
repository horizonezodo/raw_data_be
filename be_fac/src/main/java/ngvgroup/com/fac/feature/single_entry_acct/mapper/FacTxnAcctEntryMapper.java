package ngvgroup.com.fac.feature.single_entry_acct.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct_entry.FacTxnAcctEntryDTO;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntry;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacTxnAcctEntryMapper extends BaseMapper<FacTxnAcctEntryDTO, FacTxnAcctEntry> {
}
