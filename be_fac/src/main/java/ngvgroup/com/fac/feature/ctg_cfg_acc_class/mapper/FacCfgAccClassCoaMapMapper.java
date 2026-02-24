package ngvgroup.com.fac.feature.ctg_cfg_acc_class.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassCoaMapDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.model.FacCfgAccClassCoaMap;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacCfgAccClassCoaMapMapper extends BaseMapper<FacCfgAccClassCoaMapDto, FacCfgAccClassCoaMap> {
}
