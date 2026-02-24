package ngvgroup.com.bpm.features.com_cfg_process_file.service;

import java.util.List;

import ngvgroup.com.bpm.core.base.dto.ComCfgProcessFileDto;

public interface ComCfgProcessFileService {
    List<ComCfgProcessFileDto> getDetails(String processTypeCode);
}
