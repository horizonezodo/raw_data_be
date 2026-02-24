package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto.IbmCfgDepIntRateDtlDtTO;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.model.IbmCfgDepIntRateDtl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IbmCfgDepIntRateDtlMapper extends BaseMapper<IbmCfgDepIntRateDtlDtTO, IbmCfgDepIntRateDtl> {
    @Mapping(target = "interestRateCode", ignore = true)
    void updateEntityFromDto(IbmCfgDepIntRateDtlDtTO dto, @MappingTarget IbmCfgDepIntRateDtl entity);

    @Mapping(target = "id", ignore = true)
    IbmCfgDepIntRateDtl toEntity(IbmCfgDepIntRateDtlDtTO dto);

}
