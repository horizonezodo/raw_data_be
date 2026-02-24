package ngvgroup.com.fac.feature.fac_cfg_acct_entry.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request.FacCfgAcctProcessDTO;
import ngvgroup.com.fac.feature.fac_cfg_acct_entry.model.FacCfgAcctProcess;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacCfgAcctProcessMapper extends BaseMapper<FacCfgAcctProcessDTO, FacCfgAcctProcess> {
}
