package ngvgroup.com.bpmn.service;

import ngvgroup.com.bpmn.dto.ComCfgProcessType.ComCfgProcessTypeDto;

import java.util.List;

public interface ComCfgProcessTypeService {
    List<ComCfgProcessTypeDto> getListProcessType();

    List<ComCfgProcessTypeDto> getListProcessTypeByGroupCode(String processGroupCode);
}
