package ngvgroup.com.fac.feature.single_entry_acct.service.impl;

import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.fac.feature.common.dto.TemplateResDto;
import ngvgroup.com.fac.feature.common.model.ComInfOrganization;
import ngvgroup.com.fac.feature.common.repository.ComInfOrganizationRepository;
import ngvgroup.com.fac.feature.single_entry_acct.dto.SingleEntryAcctDTO;
import ngvgroup.com.fac.feature.single_entry_acct.dto.TransactionInfoDTO;
import ngvgroup.com.fac.feature.single_entry_acct.service.SingleEntryAcctBpmService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SingleEntryAcctBpmServiceImpl extends BaseStoredProcedureService implements SingleEntryAcctBpmService {

    private final ngvgroup.com.bpm.client.service.DocumentTemplateService templateService;
    private final ComInfOrganizationRepository comInfOrganizationRepository;

    // ========================================================================
    // PUBLIC METHODS (INTERFACE IMPLEMENTATION)
    // ========================================================================

    @Override
    public AttachmentContext buildAttachmentContext(String processFileCode, SingleEntryAcctDTO businessData) {
        Map<String, Object> context = new HashMap<>();
        context.put("data", businessData);

        // Fetch Org info if needed
        ComInfOrganization org = comInfOrganizationRepository.findByOrgCode(
                businessData.getTransactionInfo().getAcctEntryForm().getCommon().getOrgCode()
        );
        context.put("org", org);

        return AttachmentContext.builder()
                .processFileCode(processFileCode)
                .context(context)
                .build();
    }

    @Override
    public byte[] generateBusinessFile(SingleEntryAcctDTO businessData, TemplateResDto template) {
        // Sinh file DOCX đã được điền dữ liệu
        Map<String, Object> context = new HashMap<>();
        context.put("data", businessData);
        // Fetch Org info if needed
        ComInfOrganization org = comInfOrganizationRepository.findByOrgCode(
                businessData.getTransactionInfo().getAcctEntryForm().getCommon().getOrgCode()
        );
        context.put("org", org);

        return templateService.generateFile(
                template.getFilePath(),
                template.getMappingFilePath(),
                context);
    }

    @Override
    public MailVariableDto buildMailVariable(SubmitTaskRequest<SingleEntryAcctDTO> dto) {
        return null;
    }

    @Override
    public ProcessData buildProcessData(StartRequest<SingleEntryAcctDTO> dto, String txnContentCode) {
        TransactionInfoDTO infoDTO = dto.getBusinessData().getTransactionInfo();

        ProcessData processData = new ProcessData();
        processData.setOrgCode(infoDTO.getAcctEntryForm().getCommon().getOrgCode());
        processData.setProcessInstanceCode(infoDTO.getAcctEntryForm().getCommon().getProcessInstanceCode());
        processData.setTxnContent(txnContentCode);

        return processData;
    }
}
