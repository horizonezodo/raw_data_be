package ngvgroup.com.crm.features.customer.bpm.adjust.handler;

import org.springframework.stereotype.Component;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.response.TaskViewBpmData;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.feign.CommonFeignClient;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.template.AbstractUserTask; // Import base
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;
import ngvgroup.com.crm.features.customer.service.CustomerBpmService;
import ngvgroup.com.crm.features.customer.service.CustomerTransactionService;
import ngvgroup.com.bpm.client.annotation.UserTaskSubscription;

@Component
@UserTaskSubscription("CRM.201.01.01")
public class AdjustApproveHandler extends AbstractUserTask<CustomerProfileDTO> {

    private final CustomerTransactionService customerTransactionService;
    private final CustomerBpmService customerBpmService;

    public AdjustApproveHandler(
            BpmFeignClient bpmFeignClient,
            FileService fileService,
            CustomerTransactionService transactionService,
            CustomerBpmService bpmService,
            CommonFeignClient commonFeignClient,
            DocumentTemplateService templateService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.customerTransactionService = transactionService;
        this.customerBpmService = bpmService;
    }

    @Override
    protected void saveDraftBusinessData(CustomerProfileDTO businessData) {
        // Bước phê duyệt thường không lưu/sửa data business
    }

    @Override
    protected void validateSpecificLogic(CustomerProfileDTO businessData) {
        // Bước phê duyệt thường không validate lại data
    }

    @Override
    protected AttachmentContext createAttachmentContext(CustomerProfileDTO businessData) {
        return AttachmentContext.builder()
                .processFileCode(CrmVariableConstants.CUSTOMER_ADJUST_TEMPLATE_FILE_CODE)
                .build();
    }

    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<CustomerProfileDTO> dto) {
        return customerBpmService.buildMailVariable(CrmVariableConstants.MAIL_TEMPLATE_CODE_ADJUST, dto);
    }

    @Override
    protected CustomerProfileDTO getBusinessData(TaskViewBpmData bpmData) {
        return customerTransactionService.getDetail(bpmData.getProcessInstanceCode());
    }

    @Override
    protected String getProcessTypeCode() {
        return CrmVariableConstants.PROCESS_KEY_CUSTOMER_ADJUST;
    }
}
