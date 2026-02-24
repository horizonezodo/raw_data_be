package ngvgroup.com.loan.feature.interest_process.bpm.register.handler;

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
@UserTaskSubscription(LoanVariableConstants.INTEREST_REGISTER_EDIT_DEF)
public class RegisterEditHandler extends AbstractUserTask<InterestProfileDTO> {
    private final InterestValidationService interestValidationService;
    private final InterestTransactionService interestTransactionService;
    private final InterestBpmService interestBpmService;

    public RegisterEditHandler(BpmFeignClient bpmFeignClient, FileService fileService, CommonFeignClient commonFeignClient, DocumentTemplateService templateService, InterestValidationService interestValidationService, InterestTransactionService interestTransactionService, InterestBpmService interestBpmService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.interestValidationService = interestValidationService;
        this.interestTransactionService = interestTransactionService;
        this.interestBpmService = interestBpmService;
    }



    @Override
    protected void saveDraftBusinessData(InterestProfileDTO businessData) {
        interestTransactionService.updateInterest(businessData);
    }

    @Override
    protected void validateSpecificLogic(InterestProfileDTO businessData) {
        interestValidationService.validateCustomerInfo(businessData);
    }

    @Override
    protected String getProcessTypeCode() {
        return LoanVariableConstants.INTEREST_REGISTER_PROCESS_TYPE_CODE;
    }

    @Override
    protected AttachmentContext createAttachmentContext(InterestProfileDTO businessData) {
        return null; // Hồ sơ đính kèm của luồng không bắt buộc
    }

    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<InterestProfileDTO> dto) {
        return interestBpmService.buildMailVariable(LoanVariableConstants.MAIL_CODE_INTEREST_REGISTER, dto);
    }

    @Override
    protected InterestProfileDTO getBusinessData(TaskViewBpmData bpmData) {
        return interestTransactionService.getDetail(bpmData.getProcessInstanceCode());
    }
}



