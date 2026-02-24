package ngvgroup.com.bpm.client.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.dto.shared.ComCfgProcessFileDto;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.service.BpmClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BpmClientServiceImpl implements BpmClientService {
    private final BpmFeignClient bpmFeignClient;

    @Override
    public List<ComCfgProcessFileDto> getProcessFilesDetailByProcessTypeCode(String processTypeCode) {
        return bpmFeignClient.getProcessFiles(processTypeCode).getData();
    }
}
