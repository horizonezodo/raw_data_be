package ngvgroup.com.bpmn.service;

import ngvgroup.com.bpmn.dto.ComCfgProcessType.ComCfgProcessTypeDto;
import ngvgroup.com.bpmn.dto.CtgCfgProcessGroup.CtgCfgProcessGroupDto;

import java.util.List;

public interface CtgCfgProcessGroupService {
    List<CtgCfgProcessGroupDto> getListProcessGroup();


}
