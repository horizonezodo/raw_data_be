package ngvgroup.com.fac.feature.fac_inf_acc.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacCfgAccPurposeDto;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccPurpose;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacCfgAccPurposeMapper extends BaseMapper<FacCfgAccPurposeDto, FacCfgAccPurpose> {

}
