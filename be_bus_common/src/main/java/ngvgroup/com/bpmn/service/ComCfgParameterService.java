package ngvgroup.com.bpmn.service;

import ngvgroup.com.bpmn.dto.ComCfgParameter.ComCfgParameterDto;

public interface ComCfgParameterService {
    ComCfgParameterDto getParameterByCode(String paramCode);
}
