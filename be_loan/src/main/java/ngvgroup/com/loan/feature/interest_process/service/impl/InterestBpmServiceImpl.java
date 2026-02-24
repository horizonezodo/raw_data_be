package ngvgroup.com.loan.feature.interest_process.service.impl;

import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.InterpretiveStructureDto;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.common.service.CtgComCommonService;
import ngvgroup.com.loan.feature.interest_process.dto.InterestProfileDTO;
import ngvgroup.com.loan.feature.interest_process.service.InterestBpmService;
import ngvgroup.com.loan.feature.organization.model.ComInfOrganization;
import ngvgroup.com.loan.feature.organization.repository.ComInfOrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InterestBpmServiceImpl extends BaseStoredProcedureService implements InterestBpmService {
    private final ngvgroup.com.bpm.client.service.DocumentTemplateService templateService;
    private final CtgComCommonService commonService;
    private final ComInfOrganizationRepository comInfOrganizationRepository;

    @Override
    public AttachmentContext attachBusinessFile(String processFileCode, InterestProfileDTO businessData) {
        Map<String, Object> context = new HashMap<>();
        context.put("data", businessData);
        ComInfOrganization org = comInfOrganizationRepository.findByOrgCode(businessData.getOrgCode());
        context.put("org", org);
        return AttachmentContext.builder()
                .processFileCode(processFileCode)
                .context(context)
                .build();
    }

    @Override
    public MailVariableDto buildMailVariable(String templateCode,SubmitTaskRequest<InterestProfileDTO> dto) {
        MailVariableDto mailVariable = new MailVariableDto();

        mailVariable.setEmailTemplateCode(templateCode);
        Map<String, String> paramEmail = new HashMap<>();
        paramEmail.put(LoanVariableConstants.MAIL_PARAM_PROCESS_INSTANCE_CODE,
                dto.getBpmData().getProcessInstanceCode());
        paramEmail.put(LoanVariableConstants.MAIL_PARAM_KS_HETO, getCurrentUserName());
        paramEmail.put(LoanVariableConstants.MAIL_PARAM_COMMENT, dto.getBpmData().getTaskComment());

        mailVariable.setParamEmail(paramEmail);
        mailVariable.setUserNameCc(Collections.singletonList(getCurrentUserName()));
        return mailVariable;
    }

    @Override
    public ProcessData buildProcessData(StartRequest<InterestProfileDTO> dto, String txnContentCode) {

        ProcessData processData = new ProcessData();
        processData.setCustomerCode(null);
        processData.setCustomerName(null);
        processData.setReferenceCode(dto.getBusinessData().getInterestRateCode());
        processData.setOrgCode(dto.getBusinessData().getOrgCode());
        processData.setProcessInstanceCode(dto.getBusinessData().getProcessInstanceCode());
        processData.setTxnContent(txnContentCode);

        InterpretiveStructureDto interpretiveDto = new InterpretiveStructureDto();
        interpretiveDto.setContentCode(txnContentCode);
        interpretiveDto.setParamInterpretiveStructure(buildInterpretiveStructure(dto.getBusinessData()));
        processData.setInterpretiveStructureDto(interpretiveDto);
        return processData;
    }

    private Map<String, String> buildInterpretiveStructure(InterestProfileDTO businessData) {
        Map<String, String> interpretive = new HashMap<>();


        interpretive.put(LoanVariableConstants.ORG_CODE, businessData.getOrgCode());
        interpretive.put(LoanVariableConstants.INTEREST_RATE_TYPE, businessData.getInterestRateType());
        interpretive.put(LoanVariableConstants.APPLY_TYPE, businessData.getApplyType());
        return interpretive;
    }
}
