package ngvgroup.com.crm.features.customer.bpm.adjust.handler;

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.response.TaskViewBpmData;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.feign.CommonFeignClient;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.template.AbstractUserTask;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;
import ngvgroup.com.crm.features.customer.service.CustomerBpmService;
import ngvgroup.com.crm.features.customer.service.CustomerTransactionService;
import ngvgroup.com.crm.features.customer.service.CustomerValidationService;
import ngvgroup.com.bpm.client.annotation.UserTaskSubscription;

@Component
@UserTaskSubscription("CRM.201.01.02")
public class AdjustEditHandler extends AbstractUserTask<CustomerProfileDTO> {

    private final CustomerValidationService validationService;
    private final CustomerTransactionService transactionService;
    private final CustomerBpmService bpmService;

    public AdjustEditHandler(
            BpmFeignClient bpmFeignClient,
            FileService fileService,
            CustomerValidationService validationService,
            CustomerTransactionService transactionService,
            CustomerBpmService bpmService,
            CommonFeignClient commonFeignClient,
            DocumentTemplateService templateService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.validationService = validationService;
        this.transactionService = transactionService;
        this.bpmService = bpmService;
    }

    @Override
    @Transactional
    protected void saveDraftBusinessData(CustomerProfileDTO businessData) {
        transactionService.updateCustomer(businessData);
    }

    @Override
    protected void validateSpecificLogic(CustomerProfileDTO businessData) {
        validationService.validateCustomerInfo(businessData);
    }

    @Override
    protected AttachmentContext createAttachmentContext(CustomerProfileDTO businessData) {
        return bpmService.buildAttachmentContext(
                CrmVariableConstants.CUSTOMER_ADJUST_TEMPLATE_FILE_CODE,
                businessData);
    }

    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<CustomerProfileDTO> dto) {
        return bpmService.buildMailVariable(CrmVariableConstants.MAIL_TEMPLATE_CODE_ADJUST, dto);
    }

    @Override
    protected CustomerProfileDTO getBusinessData(TaskViewBpmData bpmData) {
        return transactionService.getDetail(bpmData.getProcessInstanceCode());
    }

    @Override
    protected String getProcessTypeCode() {
        return CrmVariableConstants.PROCESS_KEY_CUSTOMER_ADJUST;
    }
}
