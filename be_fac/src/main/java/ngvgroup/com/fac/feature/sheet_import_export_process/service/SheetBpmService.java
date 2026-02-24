package ngvgroup.com.fac.feature.sheet_import_export_process.service;

import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.SheetInfoDto;

public interface SheetBpmService {
    MailVariableDto buildMailVariable(String templateCode, SubmitTaskRequest<SheetInfoDto> dto);

    SheetInfoDto getApprovalDetail(String processInstanceCode);

    ProcessData buildProcessData(StartRequest<SheetInfoDto> dto);

}
