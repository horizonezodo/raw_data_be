package ngvgroup.com.crm.features.customer.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;

import ngvgroup.com.crm.core.constant.CrmVariableConstants;
// cleaned up unused imports
import ngvgroup.com.crm.features.common.dto.TemplateResDto;
import ngvgroup.com.crm.features.common.model.ComInfOrganization;
import ngvgroup.com.crm.features.common.repository.ComInfOrganizationRepository;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.InterpretiveStructureDto;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.crm.features.customer.dto.profile.*;
import ngvgroup.com.crm.features.customer.service.CustomerBpmService;

import org.springframework.stereotype.Service;

import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerBpmServiceImpl extends BaseStoredProcedureService implements CustomerBpmService {

    private final ngvgroup.com.bpm.client.service.DocumentTemplateService templateService;
    private final ComInfOrganizationRepository comInfOrganizationRepository;

    // ========================================================================
    // PUBLIC METHODS (INTERFACE IMPLEMENTATION)
    // ========================================================================

    @Override
    public AttachmentContext buildAttachmentContext(String processFileCode, CustomerProfileDTO businessData) {
        Map<String, Object> context = new HashMap<>();
        context.put("data", businessData);

        // Fetch Org info if needed
        CustomerGeneralInfoDTO gen = businessData.getBasicInfo();
        ComInfOrganization org = comInfOrganizationRepository.findByOrgCode(gen.getOrgCode());
        context.put("org", org);

        return AttachmentContext.builder()
                .processFileCode(processFileCode)
                .context(context)
                .build();
    }

    @Override
    public byte[] generateBusinessFile(CustomerProfileDTO businessData, TemplateResDto template) {
        // Sinh file DOCX đã được điền dữ liệu
        Map<String, Object> context = new HashMap<>();
        context.put("data", businessData);
        // Fetch Org info if needed
        CustomerGeneralInfoDTO gen = businessData.getBasicInfo();
        ComInfOrganization org = comInfOrganizationRepository.findByOrgCode(gen.getOrgCode());
        context.put("org", org);

        return templateService.generateFile(
                template.getFilePath(),
                template.getMappingFilePath(),
                context);
    }

    @Override
    public MailVariableDto buildMailVariable(String templateCode, SubmitTaskRequest<CustomerProfileDTO> dto) {
        MailVariableDto mailVariable = new MailVariableDto();

        mailVariable.setEmailTemplateCode(templateCode);

        Map<String, String> paramEmail = new HashMap<>();
        paramEmail.put(CrmVariableConstants.MAIL_PARAM_PROCESS_INSTANCE_CODE,
                dto.getBpmData().getProcessInstanceCode());
        paramEmail.put(CrmVariableConstants.MAIL_PARAM_KS_QHKH, getCurrentUserName());
        paramEmail.put(CrmVariableConstants.MAIL_PARAM_COMMENT, dto.getBpmData().getTaskComment());

        mailVariable.setParamEmail(paramEmail);
        mailVariable.setUserNameCc(Collections.singletonList(getCurrentUserName()));
        return mailVariable;
    }

    @Override
    public ProcessData buildProcessData(StartRequest<CustomerProfileDTO> dto, String txnContentCode) {
        CustomerGeneralInfoDTO genInfo = dto.getBusinessData().getBasicInfo();

        ProcessData processData = new ProcessData();
        processData.setCustomerCode(genInfo.getCustomerCode());
        processData.setCustomerName(genInfo.getCustomerName());
        processData.setReferenceCode(genInfo.getCustomerCode());
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

    // ========================================================================
    // HELPER METHODS
    // ========================================================================

    private Map<String, String> buildInterpretiveStructure(CustomerProfileDTO businessData) {
        Map<String, String> interpretive = new HashMap<>();
        CustomerGeneralInfoDTO genInfo = businessData.getBasicInfo();

        interpretive.put(CrmVariableConstants.CUSTOMER_CODE, genInfo.getCustomerCode());
        interpretive.put(CrmVariableConstants.CUSTOMER_NAME, genInfo.getCustomerName());

        if (businessData.getIdentityInfoPersonal() != null) {
            interpretive.put(CrmVariableConstants.IDENTIFICATION_ID,
                    businessData.getIdentityInfoPersonal().getIdentificationId());
        } else if (businessData.getIdentityInfoCompany() != null) {
            interpretive.put(CrmVariableConstants.IDENTIFICATION_ID,
                    businessData.getIdentityInfoCompany().getBusinessLicenseNo());
        }
        return interpretive;
    }
}
