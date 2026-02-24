package ngvgroup.com.bpm.features.common.service;

import ngvgroup.com.bpm.core.base.dto.ComCfgParameterDto;

public interface ComCfgParameterService {
    ComCfgParameterDto getParameterByCode(String paramCode);
}
