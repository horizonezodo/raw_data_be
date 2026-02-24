package ngvgroup.com.loan.feature.type_of_capital_use.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.loan.feature.type_of_capital_use.dto.LnmCfgCapitalUseDTO;
import ngvgroup.com.loan.feature.type_of_capital_use.model.LnmCfgCapitalUse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LnmCfgCapitalUseMapper extends BaseMapper<LnmCfgCapitalUseDTO, LnmCfgCapitalUse> {
}
