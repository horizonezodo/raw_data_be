package ngvgroup.com.fac.feature.fac_inf_acc.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.CurrencyTypeDto;
import ngvgroup.com.fac.feature.fac_inf_acc.model.ComInfCurrencyType;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ComInfCurrencyTypeMapper extends BaseMapper<CurrencyTypeDto, ComInfCurrencyType> {
}
