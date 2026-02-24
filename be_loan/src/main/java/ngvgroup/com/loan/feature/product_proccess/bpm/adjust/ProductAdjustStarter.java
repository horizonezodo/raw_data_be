package ngvgroup.com.loan.feature.product_proccess.bpm.adjust;

import ngvgroup.com.bpm.client.annotation.ProcessStarterSubscription;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.feign.CommonFeignClient;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.template.AbstractProcessStarter;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.product_proccess.dto.ProductProfileDTO;
import ngvgroup.com.loan.feature.product_proccess.service.ProductBpmService;
import ngvgroup.com.loan.feature.product_proccess.service.ProductTransactionService;
import ngvgroup.com.loan.feature.product_proccess.service.ProductValidationService;
import org.springframework.stereotype.Component;

@Component
@ProcessStarterSubscription(value = LoanVariableConstants.PREFIX_PRODUCT_EDIT)
public class ProductAdjustStarter extends AbstractProcessStarter<ProductProfileDTO> {

    private final ProductValidationService productValidationService;
    private final ProductTransactionService productTransactionService;
    private final ProductBpmService productBpmService;

    public ProductAdjustStarter(
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
    protected String generateAndAttachToBusinessData(StartRequest<ProductProfileDTO> dto) {
        String processInstanceCode = productTransactionService
                .getSequence()
                .generateRegistration(LoanVariableConstants.PREFIX_PRODUCT_EDIT);

        dto.getBusinessData().setProcessInstanceCode(processInstanceCode);

        return processInstanceCode;
    }

    @Override
    protected AttachmentContext createAttachmentContext(ProductProfileDTO businessData) {
        return null;
    }

    @Override
    protected void validateSpecificLogic(ProductProfileDTO businessData) {
        productValidationService.validateProductInfo(businessData);
    }

    @Override
    protected void saveBusinessData(String processDefinitionKey, ProductProfileDTO businessData) {
        productTransactionService.createProduct(businessData);
    }

    @Override
    protected ProcessData prepareProcessData(StartRequest<ProductProfileDTO> dto) {
        return productBpmService.buildProcessData(
                dto,
                LoanVariableConstants.PRODUCT_EDIT_TXN_CONTENT_CODE
        );
    }
}
