package ngvgroup.com.loan.feature.interest_process.bpm.adjust.handler;

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
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.interest_process.dto.InterestProfileDTO;
import ngvgroup.com.loan.feature.interest_process.service.InterestBpmService;
import ngvgroup.com.loan.feature.interest_process.service.InterestTransactionService;
import ngvgroup.com.loan.feature.interest_process.service.InterestValidationService;
import org.springframework.stereotype.Component;

@Component
@UserTaskSubscription(LoanVariableConstants.INTEREST_ADJUST_EDIT_DEF)
public class AdjustEditHandler extends AbstractUserTask<InterestProfileDTO> {

    private final InterestValidationService validationService;
    private final InterestTransactionService transactionService;
    private final InterestBpmService bpmService;

    public AdjustEditHandler(BpmFeignClient bpmFeignClient,
                             FileService fileService,
                             CommonFeignClient commonFeignClient,
                             DocumentTemplateService templateService,
                             InterestValidationService validationService,
                             InterestTransactionService transactionService,
                             InterestBpmService bpmService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.validationService = validationService;
        this.transactionService = transactionService;
        this.bpmService = bpmService;
    }


    @Override
    protected void saveDraftBusinessData(InterestProfileDTO businessData) {
        transactionService.updateInterest(businessData);
    }

    @Override
    protected void validateSpecificLogic(InterestProfileDTO businessData) {
        validationService.validateCustomerInfo(businessData);
    }

    @Override
    protected String getProcessTypeCode() {
        return LoanVariableConstants.INTEREST_EDIT_PROCESS_TYPE_CODE;
    }

    @Override
    protected AttachmentContext createAttachmentContext(InterestProfileDTO businessData) {
        return null;
    }

    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<InterestProfileDTO> dto) {
        return bpmService.buildMailVariable(LoanVariableConstants.MAIL_CODE_INTEREST_REGISTER, dto);
    }

    @Override
    protected InterestProfileDTO getBusinessData(TaskViewBpmData bpmData) {
        return transactionService.getDetail(bpmData.getProcessInstanceCode());
    }
}
