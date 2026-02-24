package ngvgroup.com.fac.feature.double_entry_accounting.bpm;

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
import ngvgroup.com.fac.feature.double_entry_accounting.dto.DoubleEntryAccountingProcessDto;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryBpmService;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryTransactionService;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryValidationService;
import org.springframework.stereotype.Service;

@Service
@ProcessStarterSubscription(FacVariableConstants.DOUBLE_ACCT_PROCESS_TYPE_CODE)
public class DoubleEntryRegisterStater extends AbstractProcessStarter<DoubleEntryAccountingProcessDto> {

    private final DoubleEntryValidationService doubleEntryValidationService;
    private final DoubleEntryBpmService doubleEntryBpmService;
    private final DoubleEntryTransactionService doubleEntryTransactionService;
    private final GenerateCodeService generateCodeService;

    public DoubleEntryRegisterStater(
            BpmFeignClient bpmFeignClient,
            FileService fileService,
            DoubleEntryValidationService doubleEntryValidationService,
            DoubleEntryTransactionService doubleEntryTransactionService,
            GenerateCodeService generateCodeService,
            DoubleEntryBpmService doubleEntryBpmService,
            CommonFeignClient commonFeignClient,
            DocumentTemplateService templateService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.doubleEntryValidationService = doubleEntryValidationService;
        this.doubleEntryTransactionService = doubleEntryTransactionService;
        this.doubleEntryBpmService = doubleEntryBpmService;
        this.generateCodeService=generateCodeService;
    }

    @Override
    protected void saveBusinessData(String processDefinitionKey, DoubleEntryAccountingProcessDto businessData) {

        doubleEntryTransactionService.createDoubleAccountEntry(businessData);
    }

    @Override
    public void validateSpecificLogic(DoubleEntryAccountingProcessDto businessData){
        doubleEntryValidationService.validateDoubleEntry(businessData);
    }

    @Override
    protected String generateAndAttachToBusinessData(StartRequest<DoubleEntryAccountingProcessDto> dto) {
        String processInstanceCode = generateCodeService.generateCode(
                dto.getBusinessData().getFacTxnAcctDTO().getOrgCode(),
                FacVariableConstants.PREFIX_DOUBLE_ACCT,
                FacVariableConstants.FAC_TXN_ACCT_TABLE,
                FacVariableConstants.PROCESS_INSTANCE_CODE,
                1,
                5,
                ".");
        dto.getBusinessData().getFacTxnAcctDTO().setProcessInstanceCode(processInstanceCode);

        return processInstanceCode;
    }

    @Override
    protected AttachmentContext createAttachmentContext(DoubleEntryAccountingProcessDto businessData) {
        return null;
    }

    @Override
    protected ProcessData prepareProcessData(StartRequest<DoubleEntryAccountingProcessDto> dto) {
        return doubleEntryBpmService.buildProcessData(dto, FacVariableConstants.DOUBLE_ACCT_PROCESS_TYPE_CODE,dto.getBusinessData().getFacTxnAcctDTO().getProcessInstanceCode());
    }

    public byte[] generateTemplateFile(DoubleEntryAccountingProcessDto dto, TemplateResDto template) {
        return doubleEntryBpmService.generateBusinessFile(dto, template);
    }

    public TemplateResDto getTemplateFileDetail() {
        return null;
    }
}
