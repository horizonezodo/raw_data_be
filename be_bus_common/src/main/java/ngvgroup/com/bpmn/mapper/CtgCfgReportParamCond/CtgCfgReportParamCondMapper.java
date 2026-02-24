package ngvgroup.com.bpmn.mapper.CtgCfgReportParamCond;

import ngvgroup.com.bpmn.dto.CtgCfgReportParamCond.CtgCfgReportParamCondDTO;
import ngvgroup.com.bpmn.mapper.EntityMapper;
import ngvgroup.com.bpmn.model.CtgCfgReportParamCond;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgCfgReportParamCondMapper extends EntityMapper<CtgCfgReportParamCondDTO, CtgCfgReportParamCond> {
}