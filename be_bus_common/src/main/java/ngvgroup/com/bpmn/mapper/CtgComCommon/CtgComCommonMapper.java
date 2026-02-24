package ngvgroup.com.bpmn.mapper.CtgComCommon;

import ngvgroup.com.bpmn.dto.CtgComCommon.CtgComCommonDTO;
import ngvgroup.com.bpmn.mapper.EntityMapper;
import ngvgroup.com.bpmn.model.CtgComCommon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgComCommonMapper extends EntityMapper<CtgComCommonDTO, CtgComCommon> {
}