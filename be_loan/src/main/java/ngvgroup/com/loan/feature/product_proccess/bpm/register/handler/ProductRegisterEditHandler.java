package ngvgroup.com.loan.feature.product_proccess.bpm.register.handler;

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
@UserTaskSubscription(value = LoanVariableConstants.PRODUCT_REGISTER_EDIT_DEF)
public class ProductRegisterEditHandler extends AbstractUserTask<ProductProfileDTO> {

    private final ProductValidationService productValidationService;
    private final ProductTransactionService productTransactionService;
    private final ProductBpmService productBpmService;

    public ProductRegisterEditHandler(
            BpmFeignClient bpmFeignClient,
            FileService fileService,
            ProductValidationService productValidationService,
            ProductTransactionService productTransactionService,
            ProductBpmService productBpmService,
            CommonFeignClient commonFeignClient,
            DocumentTemplateService templateService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.productValidationService = productValidationService;
        this.productTransactionService = productTransactionService;
        this.productBpmService = productBpmService;
    }

    @Override
    protected void saveDraftBusinessData(ProductProfileDTO businessData) {
        productTransactionService.updateProduct(businessData);
    }

    @Override
    protected void validateSpecificLogic(ProductProfileDTO businessData) {
        productValidationService.validateProductInfo(businessData);
    }

    @Override
    protected String getProcessTypeCode() {
        return LoanVariableConstants.PRODUCT_REGISTER_PROCESS_TYPE_CODE;
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
