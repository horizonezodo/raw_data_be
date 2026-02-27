package ngvgroup.com.bpm.client.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.dto.shared.ComCfgProcessFileDto;
import ngvgroup.com.bpm.client.dto.shared.ReportReqDto;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.feign.CommonFeignClient;
import ngvgroup.com.bpm.client.service.BpmClientService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BpmClientServiceImpl implements BpmClientService {
    private final BpmFeignClient bpmFeignClient;
    private final CommonFeignClient commonFeignClient;

    @Override
    public List<ComCfgProcessFileDto> getProcessFilesDetailByProcessTypeCode(String processTypeCode) {
        return bpmFeignClient.getProcessFiles(processTypeCode).getData();
    }

    @Override
    public ResponseEntity<byte[]> generateReport(ReportReqDto reportReqDto) {
        return commonFeignClient.generateReport(reportReqDto);
    }

}
