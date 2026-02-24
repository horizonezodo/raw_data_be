package ngvgroup.com.fac.feature.single_entry_acct.bpm.handler;

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
import ngvgroup.com.fac.feature.single_entry_acct.dto.SingleEntryAcctDTO;
import ngvgroup.com.fac.feature.single_entry_acct.service.SingleEntryAcctBpmService;
import ngvgroup.com.fac.feature.single_entry_acct.service.SingleEntryAcctService;
import ngvgroup.com.fac.feature.single_entry_acct.service.SingleEntryAcctValidatorService;
import org.springframework.stereotype.Component;

@Component
@UserTaskSubscription(FacVariableConstants.SINGLE_ENTRY_ACCT_CODE_EDIT)
public class SingleEntryAcctEditHandler extends AbstractUserTask<SingleEntryAcctDTO> {

    private final SingleEntryAcctValidatorService validationService;
    private final SingleEntryAcctService transactionService;
    private final SingleEntryAcctBpmService bpmService;

    public SingleEntryAcctEditHandler(
            BpmFeignClient bpmFeignClient,
            FileService fileService,
            SingleEntryAcctValidatorService validationService,
            SingleEntryAcctService transactionService,
            SingleEntryAcctBpmService bpmService,
            CommonFeignClient commonFeignClient,
            DocumentTemplateService templateService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.validationService = validationService;
        this.transactionService = transactionService;
        this.bpmService = bpmService;
    }

    @Override
    protected void saveDraftBusinessData(SingleEntryAcctDTO businessData) {
        // Nếu không có lưu tạm thì không cần gọi
    }

    @Override
    protected void validateSpecificLogic(SingleEntryAcctDTO businessData) {
        validationService.validateRequest(businessData);
    }

    @Override
    protected String getProcessTypeCode() {
        return FacVariableConstants.PREFIX_ACCOUNT_ENTRY_INIT;
    }

    @Override
    protected AttachmentContext createAttachmentContext(SingleEntryAcctDTO businessData) {
        // thêm sau
        return null;
    }

    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<SingleEntryAcctDTO> dto) {
        return bpmService.buildMailVariable(dto);
    }

    @Override
    protected SingleEntryAcctDTO getBusinessData(TaskViewBpmData bpmData) {
        return transactionService.getDetail(bpmData.getProcessInstanceCode());
    }
}
