package ngvgroup.com.loan.feature.interest_process.service;

import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.loan.feature.interest_process.dto.InterestProfileDTO;

public interface InterestBpmService {
    AttachmentContext attachBusinessFile(String processFileCode, InterestProfileDTO businessData);

    MailVariableDto buildMailVariable(String templateCode,SubmitTaskRequest<InterestProfileDTO> dto);

    ProcessData buildProcessData(StartRequest<InterestProfileDTO> dto, String txnContentCode);
}
