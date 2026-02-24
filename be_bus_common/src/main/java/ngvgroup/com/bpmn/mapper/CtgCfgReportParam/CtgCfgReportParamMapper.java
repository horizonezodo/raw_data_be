package ngvgroup.com.bpmn.mapper.CtgCfgReportParam;

import ngvgroup.com.bpmn.dto.CtgCfgReportParam.CtgCfgReportParamDTO;
import ngvgroup.com.bpmn.mapper.EntityMapper;
import ngvgroup.com.bpmn.model.CtgCfgReportParam;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgCfgReportParamMapper extends EntityMapper<CtgCfgReportParamDTO, CtgCfgReportParam> {
}