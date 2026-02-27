package ngvgroup.com.bpm.client.service;

import ngvgroup.com.bpm.client.dto.shared.ComCfgProcessFileDto;
import ngvgroup.com.bpm.client.dto.shared.ReportReqDto;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface BpmClientService {
    List<ComCfgProcessFileDto> getProcessFilesDetailByProcessTypeCode(String processTypeCode);

    ResponseEntity<byte[]> generateReport(ReportReqDto reportReqDto);
}
