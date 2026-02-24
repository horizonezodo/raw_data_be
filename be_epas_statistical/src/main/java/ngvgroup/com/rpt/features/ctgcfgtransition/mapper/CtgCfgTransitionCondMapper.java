package ngvgroup.com.rpt.features.ctgcfgtransition.mapper;

import ngvgroup.com.rpt.features.ctgcfgtransition.dto.CtgCfgTransitionCondDto;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgtransition.model.CtgCfgTransitionCond;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgTransitionCondMapper extends BaseMapper<CtgCfgTransitionCondDto, CtgCfgTransitionCond> {
    CtgCfgTransitionCondMapper INSTANCE = Mappers.getMapper(CtgCfgTransitionCondMapper.class);

    void updateEntity(CtgCfgTransitionCondDto dto, @MappingTarget CtgCfgTransitionCond entity);
}
