package ngvgroup.com.fac.feature.double_entry_accounting.bpm.handler;

import ngvgroup.com.bpm.client.annotation.UserTaskSubscription;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.response.TaskViewBpmData;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.feign.CommonFeignClient;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.template.AbstractUserTask;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.double_entry_accounting.dto.DoubleEntryAccountingProcessDto;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryBpmService;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryTransactionService;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryValidationService;
import org.springframework.stereotype.Component;

@Component
@UserTaskSubscription(FacVariableConstants.DOUBLE_ENTRY_ACCT_EDIT)
public class RegisterEditHandler extends AbstractUserTask<DoubleEntryAccountingProcessDto> {
    private final DoubleEntryValidationService doubleEntryValidationService;
    private final DoubleEntryTransactionService doubleEntryTransactionService;
    private final DoubleEntryBpmService doubleEntryBpmService;

    public RegisterEditHandler(
            BpmFeignClient bpmFeignClient,
            FileService fileService,
            DoubleEntryTransactionService doubleEntryTransactionService,
            DoubleEntryBpmService doubleEntryBpmService,
            DoubleEntryValidationService doubleEntryValidationService,
            CommonFeignClient commonFeignClient,
            DocumentTemplateService templateService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.doubleEntryValidationService = doubleEntryValidationService;
        this.doubleEntryTransactionService = doubleEntryTransactionService;
        this.doubleEntryBpmService = doubleEntryBpmService;
    }

    @Override
    protected void saveDraftBusinessData(DoubleEntryAccountingProcessDto businessData) {
        //add logic then
    }

    @Override
    protected void validateSpecificLogic(DoubleEntryAccountingProcessDto businessData) {
        doubleEntryValidationService.validateDoubleEntry(businessData);
    }

    @Override
    protected String getProcessTypeCode() {
        return FacVariableConstants.DOUBLE_ACCT_PROCESS_TYPE_CODE;
    }

    @Override
    protected AttachmentContext createAttachmentContext(DoubleEntryAccountingProcessDto businessData) {
        return null;
    }

    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<DoubleEntryAccountingProcessDto> dto) {
        return doubleEntryBpmService.buildMailVariable(FacVariableConstants.MAIL_TEMPLATE_CODE_DOUBLE_ENTRY, dto);
    }

    @Override
    protected DoubleEntryAccountingProcessDto getBusinessData(TaskViewBpmData bpmData) {
        return doubleEntryTransactionService.getDetail(bpmData.getProcessInstanceCode());
    }

}
