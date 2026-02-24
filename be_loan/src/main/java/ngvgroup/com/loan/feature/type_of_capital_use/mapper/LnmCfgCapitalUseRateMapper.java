package ngvgroup.com.loan.feature.type_of_capital_use.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.loan.feature.type_of_capital_use.dto.LnmCfgCapitalUseRateDTO;
import ngvgroup.com.loan.feature.type_of_capital_use.model.LnmCfgCapitalUseRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LnmCfgCapitalUseRateMapper extends BaseMapper<LnmCfgCapitalUseRateDTO, LnmCfgCapitalUseRate> {
    LnmCfgCapitalUseRateMapper INSTANCE = Mappers.getMapper(LnmCfgCapitalUseRateMapper.class);


    @Override
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(LnmCfgCapitalUseRateDTO dto, @MappingTarget LnmCfgCapitalUseRate entity);
}
