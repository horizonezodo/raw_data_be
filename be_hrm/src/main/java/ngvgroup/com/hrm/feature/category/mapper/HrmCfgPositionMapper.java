package ngvgroup.com.hrm.feature.category.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.hrm.feature.category.dto.HrmCfgPositionDTO;
import ngvgroup.com.hrm.feature.category.model.HrmCfgPosition;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HrmCfgPositionMapper extends BaseMapper<HrmCfgPositionDTO, HrmCfgPosition> {
}
