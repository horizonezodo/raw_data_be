package ngvgroup.com.bpmn.mapper.CtgCfgReport;

import ngvgroup.com.bpmn.dto.CtgCfgReport.CtgCfgReportDTO;
import ngvgroup.com.bpmn.mapper.EntityMapper;
import ngvgroup.com.bpmn.model.CtgCfgReport;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgCfgReportMapper extends EntityMapper<CtgCfgReportDTO, CtgCfgReport> {
}