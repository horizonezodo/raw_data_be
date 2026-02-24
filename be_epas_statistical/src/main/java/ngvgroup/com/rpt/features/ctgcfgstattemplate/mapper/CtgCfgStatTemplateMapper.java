package ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.CtgCfgStatTemplateDto;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgStatTemplateMapper extends BaseMapper<CtgCfgStatTemplateDto, CtgCfgStatTemplate> {
    CtgCfgStatTemplateMapper INSTANCE= Mappers.getMapper(CtgCfgStatTemplateMapper.class);
}
