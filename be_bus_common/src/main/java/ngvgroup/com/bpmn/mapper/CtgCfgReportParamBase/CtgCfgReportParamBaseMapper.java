package ngvgroup.com.bpmn.mapper.CtgCfgReportParamBase;

import ngvgroup.com.bpmn.dto.CtgCfgReportParamBase.CtgCfgReportParamBaseDto;
import ngvgroup.com.bpmn.dto.CtgCfgReportParamBase.CtgCfgReportParamBaseResponse;
import ngvgroup.com.bpmn.model.CtgCfgReportParamBase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CtgCfgReportParamBaseMapper {
    CtgCfgReportParamBase toEntity(CtgCfgReportParamBaseDto paramBaseDto);

    CtgCfgReportParamBaseDto toDto(CtgCfgReportParamBase paramBase);
    CtgCfgReportParamBaseResponse toResponse(CtgCfgReportParamBase paramBase);

    @Mapping(target = "paramBaseCode", ignore = true)
    void updateComCfgReportParamBase(CtgCfgReportParamBaseDto dto, @MappingTarget CtgCfgReportParamBase entity);


}
