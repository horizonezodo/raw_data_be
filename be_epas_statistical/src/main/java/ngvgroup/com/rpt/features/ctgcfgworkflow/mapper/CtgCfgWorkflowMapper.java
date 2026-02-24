package ngvgroup.com.rpt.features.ctgcfgworkflow.mapper;

import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.CtgCfgWorkflowDto;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.CtgCfgWorkflow;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgWorkflowMapper extends BaseMapper<CtgCfgWorkflowDto, CtgCfgWorkflow> {
    CtgCfgWorkflowMapper INSTANCE = Mappers.getMapper(CtgCfgWorkflowMapper.class);

    void updateEntity(CtgCfgWorkflowDto dto, @MappingTarget CtgCfgWorkflow entity);
}
