package ngvgroup.com.fac.feature.regulated_account.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaAccDTO;
import ngvgroup.com.fac.feature.regulated_account.model.CtgCfgCoaAcc;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgCoaAccMapper extends BaseMapper<CtgCfgCoaAccDTO, CtgCfgCoaAcc> {

}
