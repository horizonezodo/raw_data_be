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
import ngvgroup.com.loan.feature.product_proccess.service.ProductValidationService;
import org.springframework.stereotype.Component;

@Component
@UserTaskSubscription(value = LoanVariableConstants.PRODUCT_ADJUST_EDIT_DEF)
public class ProductAdjustEditHandler extends AbstractUserTask<ProductProfileDTO> {

    private final ProductValidationService validationService;
    private final ProductTransactionService transactionService;
    private final ProductBpmService bpmService;

    public ProductAdjustEditHandler(BpmFeignClient bpmFeignClient,
                                    FileService fileService,
                                    ProductValidationService validationService,
                                    ProductTransactionService transactionService,
                                    ProductBpmService bpmService,
                                    CommonFeignClient commonFeignClient,
                                    DocumentTemplateService templateService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.validationService = validationService;
        this.transactionService = transactionService;
        this.bpmService = bpmService;
    }

    @Override
    protected void saveDraftBusinessData(ProductProfileDTO businessData) {
        transactionService.updateProduct(businessData);

    }

    @Override
    protected void validateSpecificLogic(ProductProfileDTO businessData) {
        validationService.validateProductInfo(businessData);
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
        return bpmService.buildMailVariable(dto);
    }

    @Override
    protected ProductProfileDTO getBusinessData(TaskViewBpmData bpmData) {
        return transactionService.getDetail(bpmData.getProcessInstanceCode());
    }
}
