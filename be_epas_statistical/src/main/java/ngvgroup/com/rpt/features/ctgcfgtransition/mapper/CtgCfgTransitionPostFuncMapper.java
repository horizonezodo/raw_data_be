package ngvgroup.com.rpt.features.ctgcfgtransition.mapper;

import ngvgroup.com.rpt.features.ctgcfgtransition.dto.CtgCfgTransitionPostFuncDto;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgtransition.model.CtgCfgTransitionPostFunc;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgTransitionPostFuncMapper
        extends BaseMapper<CtgCfgTransitionPostFuncDto, CtgCfgTransitionPostFunc> {
    CtgCfgTransitionPostFuncMapper INSTANCE = Mappers.getMapper(CtgCfgTransitionPostFuncMapper.class);

    void updateEntity(CtgCfgTransitionPostFuncDto dto, @MappingTarget CtgCfgTransitionPostFunc entity);
}
