package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto.IbmCfgDepIntRateDTO;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto.IbmCfgDepIntRateSearch;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.model.IbmCfgDepIntRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IbmCfgDepIntRateMapper extends BaseMapper<IbmCfgDepIntRateDTO, IbmCfgDepIntRate> {
    @Mapping(target = "interestRateCode", ignore = true)
    void updateEntityFromDto(IbmCfgDepIntRateDTO dto, @MappingTarget IbmCfgDepIntRate entity);

    IbmCfgDepIntRateSearch toSearch(IbmCfgDepIntRate entity);
}
