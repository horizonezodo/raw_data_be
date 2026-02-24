package ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatedeadline.CtgCfgStatTemplateDeadLineDTO;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateDeadLine;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgStatTemplateDeadLineMapper extends BaseMapper<CtgCfgStatTemplateDeadLineDTO, CtgCfgStatTemplateDeadLine> {
    CtgCfgStatTemplateDeadLineMapper INSTANCE= Mappers.getMapper(CtgCfgStatTemplateDeadLineMapper.class);
}
