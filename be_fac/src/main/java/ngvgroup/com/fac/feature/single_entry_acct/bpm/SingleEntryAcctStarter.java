package ngvgroup.com.fac.feature.single_entry_acct.bpm;

import ngvgroup.com.bpm.client.annotation.ProcessStarterSubscription;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.feign.CommonFeignClient;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.template.AbstractProcessStarter;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.common.component.GenerateCodeService;
import ngvgroup.com.fac.feature.common.dto.TemplateResDto;
import ngvgroup.com.fac.feature.common.service.CtgCfgTemplateService;
import ngvgroup.com.fac.feature.single_entry_acct.dto.SingleEntryAcctDTO;
import ngvgroup.com.fac.feature.single_entry_acct.service.SingleEntryAcctBpmService;
import ngvgroup.com.fac.feature.single_entry_acct.service.SingleEntryAcctService;
import ngvgroup.com.fac.feature.single_entry_acct.service.SingleEntryAcctValidatorService;
import org.springframework.stereotype.Component;

@Component
@ProcessStarterSubscription(FacVariableConstants.PREFIX_ACCOUNT_ENTRY_INIT)
public class SingleEntryAcctStarter extends AbstractProcessStarter<SingleEntryAcctDTO> {
    private final SingleEntryAcctService entryAccountingService;
    private final GenerateCodeService generateCodeService;
    private final SingleEntryAcctValidatorService validatorService;
    private final SingleEntryAcctBpmService bpmService;
    private final CtgCfgTemplateService ctgCfgTemplateService;

    public SingleEntryAcctStarter(
            BpmFeignClient bpmFeignClient,
            FileService fileService,
            SingleEntryAcctService entryAccountingService,
            GenerateCodeService generateCodeService,
            SingleEntryAcctValidatorService validatorService,
            SingleEntryAcctBpmService bpmService,
            CommonFeignClient commonFeignClient,
            DocumentTemplateService templateService, CtgCfgTemplateService ctgCfgTemplateService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.entryAccountingService = entryAccountingService;
        this.generateCodeService = generateCodeService;
        this.validatorService = validatorService;
        this.bpmService = bpmService;
        this.ctgCfgTemplateService = ctgCfgTemplateService;
    }

    @Override
    protected void validateSpecificLogic(SingleEntryAcctDTO businessData) {
        validatorService.validateRequest(businessData);
    }

    @Override
    protected void saveBusinessData(String processDefinitionKey, SingleEntryAcctDTO businessData) {
        String processInstanceCode = businessData.getTransactionInfo().getAcctEntryForm().getCommon().getProcessInstanceCode();

        entryAccountingService.createAccountEntry(businessData, processInstanceCode);

    }

    @Override
    protected String generateAndAttachToBusinessData(StartRequest<SingleEntryAcctDTO> dto) {

        String orgCode = dto.getBusinessData().getTransactionInfo().getAcctEntryForm().getCommon().getOrgCode();
        String processInstanceCode = generateCodeService.generateCode(
                orgCode,
                FacVariableConstants.PREFIX_ACCOUNT_ENTRY_INIT,
                FacVariableConstants.FAC_TXN_ACCT_TABLE,
                FacVariableConstants.PROCESS_INSTANCE_CODE,
                1,
                5,
                ".");
        dto.getBusinessData().getTransactionInfo().getAcctEntryForm().getCommon().setProcessInstanceCode(processInstanceCode);

        return processInstanceCode;
    }

    @Override
    protected AttachmentContext createAttachmentContext(SingleEntryAcctDTO businessData) {
        //Add then
        return null;
    }

    @Override
    protected ProcessData prepareProcessData(StartRequest<SingleEntryAcctDTO> dto) {

        return bpmService.buildProcessData(dto, FacVariableConstants.SINGLE_ENTRY_ACCT_CODE_INIT);
    }

    public byte[] generateTemplateFile(SingleEntryAcctDTO dto, TemplateResDto template) {
        return bpmService.generateBusinessFile(dto, template);
    }

    public TemplateResDto getTemplateFileDetail() {
        return ctgCfgTemplateService.getTemplateByCode(FacVariableConstants.SINGLE_ENTRY_ACCT_TEMPLATE_FILE_CODE);
    }
}
