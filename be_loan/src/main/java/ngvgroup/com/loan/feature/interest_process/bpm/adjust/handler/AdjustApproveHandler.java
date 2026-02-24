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
import org.springframework.stereotype.Component;

@Component
@UserTaskSubscription(LoanVariableConstants.INTEREST_ADJUST_APPROVE_DEF)
public class AdjustApproveHandler extends AbstractUserTask<InterestProfileDTO> {
    private final InterestTransactionService interestTransactionService;
    private final InterestBpmService interestBpmService;

    public AdjustApproveHandler(BpmFeignClient bpmFeignClient,
                                FileService fileService,
                                CommonFeignClient commonFeignClient,
                                DocumentTemplateService templateService,
                                InterestTransactionService interestTransactionService,
                                InterestBpmService interestBpmService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.interestTransactionService = interestTransactionService;
        this.interestBpmService = interestBpmService;
    }


    @Override
    protected void saveDraftBusinessData(InterestProfileDTO businessData) {
        //
    }

    @Override
    protected void validateSpecificLogic(InterestProfileDTO businessData) {
        //
    }

    @Override
    protected String getProcessTypeCode() {
        return LoanVariableConstants.INTEREST_EDIT_PROCESS_TYPE_CODE;
    }

    @Override
    protected AttachmentContext createAttachmentContext(InterestProfileDTO businessData) {
        return null; // Hồ sơ đính kèm của luồng không bắt buộc
    }

    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<InterestProfileDTO> dto) {
        return interestBpmService.buildMailVariable(LoanVariableConstants.MAIL_CODE_INTEREST_UPDATE, dto);
    }

    @Override
    protected InterestProfileDTO getBusinessData(TaskViewBpmData bpmData) {
        return interestTransactionService.getDetail(bpmData.getProcessInstanceCode());
    }
}
