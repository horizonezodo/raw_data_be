package ngvgroup.com.loan.feature.collateral_type.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ngvgroup.com.loan.feature.collateral_type.dto.CtgCfgCollateralTypeDto;
import ngvgroup.com.loan.feature.collateral_type.model.CtgCfgCollateralType;

@Mapper(componentModel = "spring")
public interface CtgCfgCollateralTypeMapper extends BaseMapper<CtgCfgCollateralTypeDto, CtgCfgCollateralType> {

    @Mapping(target = "collateralTypeCode", ignore = true)
    void updateCtgCfgCollateralTypeFromDto(CtgCfgCollateralTypeDto dto, @MappingTarget CtgCfgCollateralType entity);
}
