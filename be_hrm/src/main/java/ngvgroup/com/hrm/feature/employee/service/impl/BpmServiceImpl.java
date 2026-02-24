package ngvgroup.com.hrm.feature.employee.service.impl;

import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.variable.InterpretiveStructureDto;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.hrm.core.constant.HrmVariableConstants;
import ngvgroup.com.hrm.feature.employee.dto.HrmBasicInfoDto;
import ngvgroup.com.hrm.feature.employee.dto.HrmProfileDto;
import ngvgroup.com.hrm.feature.employee.service.BpmService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class BpmServiceImpl extends BaseStoredProcedureService implements BpmService {

    @Override
    public MailVariableDto buildMailVariable(String templateCode, SubmitTaskRequest<HrmProfileDto> dto) {
        MailVariableDto mailVariable = new MailVariableDto();

        mailVariable.setEmailTemplateCode(templateCode);

        Map<String, String> paramEmail = new HashMap<>();
        paramEmail.put(HrmVariableConstants.MAIL_PARAM_PROCESS_INSTANCE_CODE,
                dto.getBpmData().getProcessInstanceCode());
        paramEmail.put(HrmVariableConstants.MAIL_PARAM_KS_HSNS, getCurrentUserName());
        paramEmail.put(HrmVariableConstants.MAIL_PARAM_COMMENT, dto.getBpmData().getTaskComment());

        mailVariable.setParamEmail(paramEmail);
        mailVariable.setUserNameCc(Collections.singletonList(getCurrentUserName()));
        return mailVariable;
    }

    @Override
    public ProcessData buildProcessData(StartRequest<HrmProfileDto> dto, String txnContentCode) {
        HrmBasicInfoDto genInfo = dto.getBusinessData().getBasicInfo();

        ProcessData processData = new ProcessData();
        processData.setReferenceCode(genInfo.getEmployeeCode());
        processData.setOrgCode(genInfo.getOrgCode());
        processData.setProcessInstanceCode(genInfo.getProcessInstanceCode());
        processData.setTxnContent(txnContentCode);

        // Interpretive Structure
        InterpretiveStructureDto interpretiveDto = new InterpretiveStructureDto();
        interpretiveDto.setContentCode(txnContentCode);
        interpretiveDto.setParamInterpretiveStructure(buildInterpretiveStructure(dto.getBusinessData()));
        processData.setInterpretiveStructureDto(interpretiveDto);

        return processData;
    }

    private Map<String, String> buildInterpretiveStructure(HrmProfileDto businessData) {
        Map<String, String> interpretive = new HashMap<>();
        HrmBasicInfoDto genInfo = businessData.getBasicInfo();

        interpretive.put(HrmVariableConstants.EMPLOYEE_CODE, genInfo.getEmployeeCode());
        interpretive.put(HrmVariableConstants.EMPLOYEE_NAME, genInfo.getEmployeeName());
        interpretive.put(HrmVariableConstants.IDENTIFICATION_ID, genInfo.getIdentificationId());
        return interpretive;
    }

}
