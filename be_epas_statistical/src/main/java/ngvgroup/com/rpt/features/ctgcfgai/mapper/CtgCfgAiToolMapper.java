package ngvgroup.com.rpt.features.ctgcfgai.mapper;

import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTO;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgai.model.CtgCfgAiTool;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgAiToolMapper extends BaseMapper<CtgCfgAiToolDTO, CtgCfgAiTool> {
    CtgCfgAiToolMapper INSTANCE= Mappers.getMapper(CtgCfgAiToolMapper.class);
}
