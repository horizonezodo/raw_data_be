package ngvgroup.com.bpm.client.service;

import ngvgroup.com.bpm.client.dto.shared.ComCfgProcessFileDto;

import java.util.List;

public interface BpmClientService {
    List<ComCfgProcessFileDto> getProcessFilesDetailByProcessTypeCode(String processTypeCode);
}
