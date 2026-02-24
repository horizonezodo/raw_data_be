package ngvgroup.com.fac.feature.fac_cfg_acct_entry.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctEntryDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctEntry;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacCfgAcctEntryMapper extends BaseMapper<FacCfgAcctEntryDTO, FacCfgAcctEntry> {
}
