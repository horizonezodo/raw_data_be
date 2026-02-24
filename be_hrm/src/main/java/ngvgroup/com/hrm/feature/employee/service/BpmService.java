package ngvgroup.com.hrm.feature.employee.service;

import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.hrm.feature.employee.dto.HrmProfileDto;

public interface BpmService {
    MailVariableDto buildMailVariable(String templateCode, SubmitTaskRequest<HrmProfileDto> dto);

    ProcessData buildProcessData(StartRequest<HrmProfileDto> dto, String txnContentCode);
}
