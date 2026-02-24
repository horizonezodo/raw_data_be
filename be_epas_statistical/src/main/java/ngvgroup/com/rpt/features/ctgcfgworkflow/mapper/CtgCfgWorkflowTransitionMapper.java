package ngvgroup.com.rpt.features.ctgcfgworkflow.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.workflowtransition.WorkflowTransitionDto;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.WorkflowTransition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CtgCfgWorkflowTransitionMapper extends BaseMapper<WorkflowTransitionDto, WorkflowTransition> {
    CtgCfgWorkflowTransitionMapper INSTANCE = Mappers.getMapper(CtgCfgWorkflowTransitionMapper.class);

    void updateEntity(WorkflowTransitionDto dto, @MappingTarget WorkflowTransition entity);
}
