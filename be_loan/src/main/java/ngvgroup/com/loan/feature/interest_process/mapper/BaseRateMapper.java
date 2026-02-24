package ngvgroup.com.loan.feature.interest_process.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.loan.feature.interest_process.dto.BaseRateDTO;
import ngvgroup.com.loan.feature.interest_process.model.ComCfgBaseRate;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BaseRateMapper extends BaseMapper<BaseRateDTO, ComCfgBaseRate> {
    ComCfgBaseRate INSTANCE = Mappers.getMapper(ComCfgBaseRate.class);
}
