package ngvgroup.com.rpt.features.report.service;

import ngvgroup.com.rpt.features.report.dto.ComCfgParameterDto;

public interface ComCfgParameterService {
    ComCfgParameterDto getParameterByCode(String paramCode);
}
