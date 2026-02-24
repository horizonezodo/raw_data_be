package ngvgroup.com.loan.feature.product_proccess.bpm.adjust.handler;

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
import ngvgroup.com.loan.feature.product_proccess.dto.ProductProfileDTO;
import ngvgroup.com.loan.feature.product_proccess.service.ProductBpmService;
import ngvgroup.com.loan.feature.product_proccess.service.ProductTransactionService;
import org.springframework.stereotype.Component;

@Component
@UserTaskSubscription(value = LoanVariableConstants.PRODUCT_ADJUST_APPROVE_DEF)
public class ProductAdjustApproveHandler extends AbstractUserTask<ProductProfileDTO> {

    private final ProductTransactionService productTransactionService;
    private final ProductBpmService productBpmService;

    public ProductAdjustApproveHandler(BpmFeignClient bpmFeignClient,
                                       FileService fileService,
                                       ProductTransactionService productTransactionService,
                                       ProductBpmService productBpmService,
                                       CommonFeignClient commonFeignClient,
                                       DocumentTemplateService templateService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.productTransactionService = productTransactionService;
        this.productBpmService = productBpmService;
    }

    @Override
    protected void saveDraftBusinessData(ProductProfileDTO businessData) {
        //
    }

    @Override
    protected void validateSpecificLogic(ProductProfileDTO businessData) {
        // Thường không validate lại ở bước approve chỉnh sửa
    }

    @Override
    protected String getProcessTypeCode() {
        return LoanVariableConstants.PRODUCT_EDIT_PROCESS_TYPE_CODE;
    }

    @Override
    protected AttachmentContext createAttachmentContext(ProductProfileDTO businessData) {
        return null;
    }

    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<ProductProfileDTO> dto) {
        return productBpmService.buildMailVariable(dto);
    }

    @Override
    protected ProductProfileDTO getBusinessData(TaskViewBpmData bpmData) {
        return productTransactionService.getDetail(bpmData.getProcessInstanceCode());
    }
}
