package ngvgroup.com.hrm.feature.hrm_cfg_status.service;

import ngvgroup.com.hrm.feature.hrm_cfg_status.dto.HrmCfgStatusOptionDto;

import java.util.List;

public interface HrmCfgStatusService {
    List<HrmCfgStatusOptionDto> listOptions();
}
