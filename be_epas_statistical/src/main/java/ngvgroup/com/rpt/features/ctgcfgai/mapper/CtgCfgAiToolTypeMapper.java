package ngvgroup.com.rpt.features.ctgcfgai.mapper;

import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitooltype.CtgCfgAiToolTypeDTO;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgai.model.CtgCfgAiToolType;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgAiToolTypeMapper extends BaseMapper<CtgCfgAiToolTypeDTO, CtgCfgAiToolType> {
    CtgCfgAiToolTypeMapper INSTANCE= Mappers.getMapper(CtgCfgAiToolTypeMapper.class);
}
