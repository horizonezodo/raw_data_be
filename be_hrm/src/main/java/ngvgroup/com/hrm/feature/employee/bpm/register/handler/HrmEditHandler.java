package ngvgroup.com.hrm.feature.employee.bpm.register.handler;

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
import ngvgroup.com.hrm.core.constant.HrmVariableConstants;
import ngvgroup.com.hrm.feature.employee.dto.HrmProfileDto;
import ngvgroup.com.hrm.feature.employee.service.BpmService;
import ngvgroup.com.hrm.feature.employee.service.TransactionService;
import ngvgroup.com.hrm.feature.employee.service.ValidationSerivce;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@UserTaskSubscription("HRM.200.01.02")
public class HrmEditHandler extends AbstractUserTask<HrmProfileDto> {

    private final TransactionService transactionService;
    private final ValidationSerivce validationSerivce;
    private final BpmService bpmService;

    public HrmEditHandler(BpmFeignClient bpmFeignClient, FileService fileService,
            CommonFeignClient commonFeignClient, DocumentTemplateService templateService,
            TransactionService transactionService, ValidationSerivce validationSerivce, BpmService bpmService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.transactionService = transactionService;
        this.validationSerivce = validationSerivce;
        this.bpmService = bpmService;
    }

    @Override
    @Transactional
    protected void saveDraftBusinessData(HrmProfileDto businessData) {
        transactionService.updateEmployee(businessData.getBasicInfo().getProcessInstanceCode(), businessData);
    }

    @Override
    protected void validateSpecificLogic(HrmProfileDto businessData) {
        validationSerivce.validateHrmProfile(businessData);
    }

    @Override
    protected String getProcessTypeCode() {
        return HrmVariableConstants.PROCESS_KEY_HRM_REGISTER;
    }

    @Override
    protected AttachmentContext createAttachmentContext(HrmProfileDto businessData) {
        return null;
    }

    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<HrmProfileDto> dto) {
        return bpmService.buildMailVariable(HrmVariableConstants.DD, dto);
    }

    @Override
    protected HrmProfileDto getBusinessData(TaskViewBpmData bpmData) {
        return transactionService.getProfile(bpmData.getProcessInstanceCode());
    }
}
