package ngvgroup.com.fac.feature.ctg_cfg_acc_class.mapper;


import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.model.FacCfgAccClass;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacCfgAccClassMapper extends BaseMapper<FacCfgAccClassDto, FacCfgAccClass> {

}
