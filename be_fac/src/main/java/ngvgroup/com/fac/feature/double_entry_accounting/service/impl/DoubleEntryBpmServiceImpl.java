package ngvgroup.com.fac.feature.double_entry_accounting.service.impl;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import lombok.AllArgsConstructor;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.InterpretiveStructureDto;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.common.dto.TemplateResDto;
import ngvgroup.com.fac.feature.common.model.ComInfOrganization;
import ngvgroup.com.fac.feature.common.repository.ComInfOrganizationRepository;
import ngvgroup.com.fac.feature.double_entry_accounting.dto.DoubleEntryAccountingProcessDto;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryBpmService;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct.FacTxnAcctDTO;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoubleEntryBpmServiceImpl extends BaseStoredProcedureService implements DoubleEntryBpmService {

    private final ComInfOrganizationRepository comInfOrganizationRepository;
    private final DocumentTemplateService templateService;
    @Override
    public ProcessData buildProcessData(StartRequest<DoubleEntryAccountingProcessDto> dto, String txnContentCode,String processInstanceCode){
        DoubleEntryAccountingProcessDto doubleEntryAccountingProcessReqDto=dto.getBusinessData();
        ProcessData processData = new ProcessData();
        processData.setCustomerCode(doubleEntryAccountingProcessReqDto.getFacTxnAcctDTO().getObjectTxnCode());
        processData.setCustomerName(doubleEntryAccountingProcessReqDto.getFacTxnAcctDTO().getObjectTxnName());
        processData.setReferenceCode(doubleEntryAccountingProcessReqDto.getFacTxnAcctDTO().getObjectTxnCode());
        processData.setOrgCode(doubleEntryAccountingProcessReqDto.getFacTxnAcctDTO().getOrgCode());
        processData.setProcessInstanceCode(processInstanceCode);
        processData.setTxnContent(txnContentCode);

        // Interpretive Structure
        InterpretiveStructureDto interpretiveDto = new InterpretiveStructureDto();
        interpretiveDto.setContentCode(txnContentCode);
        interpretiveDto.setParamInterpretiveStructure(buildInterpretiveStructure(dto.getBusinessData()));
        processData.setInterpretiveStructureDto(interpretiveDto);
        return processData;
    }

    private Map<String, String> buildInterpretiveStructure(DoubleEntryAccountingProcessDto businessData) {
        Map<String, String> interpretive = new HashMap<>();
        interpretive.put(FacVariableConstants.CUSTOMER_CODE, businessData.getFacTxnAcctDTO().getObjectTxnCode());
        interpretive.put(FacVariableConstants.CUSTOMER_NAME, businessData.getFacTxnAcctDTO().getObjectTxnName());
        return interpretive;
    }

    @Override
    public AttachmentContext buildAttachmentContext(String processFileCode, DoubleEntryAccountingProcessDto businessData) {
        Map<String, Object> context = new HashMap<>();
        context.put("data", businessData);

        // Fetch Org info if needed
        FacTxnAcctDTO gen = businessData.getFacTxnAcctDTO();
        ComInfOrganization org = comInfOrganizationRepository.findByOrgCode(gen.getOrgCode());
        context.put("org", org);

        return AttachmentContext.builder()
                .processFileCode(processFileCode)
                .context(context)
                .build();
    }

    @Override
    public byte[] generateBusinessFile(DoubleEntryAccountingProcessDto businessData, TemplateResDto template) {

        Map<String, Object> context = new HashMap<>();
        context.put("data", businessData);

        FacTxnAcctDTO gen = businessData.getFacTxnAcctDTO();
        ComInfOrganization org = comInfOrganizationRepository.findByOrgCode(gen.getOrgCode());
        context.put("org", org);

        return templateService.generateFile(
                template.getFilePath(),
                template.getMappingFilePath(),
                context);
    }

    @Override
    public MailVariableDto buildMailVariable(String templateCode, SubmitTaskRequest<DoubleEntryAccountingProcessDto> dto) {
        MailVariableDto mailVariable = new MailVariableDto();
        mailVariable.setEmailTemplateCode(templateCode);

        Map<String, Object> paramEmail = new HashMap<>();
        paramEmail.put(FacVariableConstants.MAIL_PARAM_PROCESS_INSTANCE_CODE,
                dto.getBusinessData().getFacTxnAcctDTO().getProcessInstanceCode());
        paramEmail.put(FacVariableConstants.MAIL_PARAM_KS_QHKH, getCurrentUserName());
        paramEmail.put(FacVariableConstants.MAIL_PARAM_COMMENT, dto.getBpmData().getTaskComment());

        Map<String, String> paramEmailString = paramEmail.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() != null ? e.getValue().toString() : ""
                ));
        mailVariable.setParamEmail(paramEmailString);
        mailVariable.setUserNameCc(Collections.singletonList(getCurrentUserName()));
        return mailVariable;
    }

}
