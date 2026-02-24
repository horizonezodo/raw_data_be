package ngvgroup.com.bpmn.mapper.CtgCfgReportGroup;

import ngvgroup.com.bpmn.dto.CtgCfgReportGroupDTO.CtgCfgReportGroupDTO;
import ngvgroup.com.bpmn.mapper.EntityMapper;
import ngvgroup.com.bpmn.model.CtgCfgReportGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgCfgReportGroupMapper extends EntityMapper<CtgCfgReportGroupDTO, CtgCfgReportGroup> {
}