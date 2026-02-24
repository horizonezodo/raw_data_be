package ngvgroup.com.fac.feature.single_entry_acct.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct.FacTxnAcctDTO;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcct;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacTxnAcctMapper extends BaseMapper<FacTxnAcctDTO, FacTxnAcct> {
}
