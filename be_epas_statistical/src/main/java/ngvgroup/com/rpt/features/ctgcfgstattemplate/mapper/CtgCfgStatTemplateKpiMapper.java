package ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatekpi.CtgCfgStatTemplateKpiDTO;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateKpi;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgStatTemplateKpiMapper extends BaseMapper<CtgCfgStatTemplateKpiDTO, CtgCfgStatTemplateKpi> {
    CtgCfgStatTemplateKpiMapper INSTANCE= Mappers.getMapper(CtgCfgStatTemplateKpiMapper.class);
}
