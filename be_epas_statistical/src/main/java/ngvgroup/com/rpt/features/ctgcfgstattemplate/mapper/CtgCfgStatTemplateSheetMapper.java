package ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplatesheet.CtgCfgStatTemplateSheetDTO;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatTemplateSheet;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgStatTemplateSheetMapper extends BaseMapper<CtgCfgStatTemplateSheetDTO, CtgCfgStatTemplateSheet> {
    CtgCfgStatTemplateSheetDTO INSTANCE= Mappers.getMapper(CtgCfgStatTemplateSheetDTO.class);
}
