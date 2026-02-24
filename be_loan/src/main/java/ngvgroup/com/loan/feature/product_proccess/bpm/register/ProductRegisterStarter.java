package ngvgroup.com.loan.feature.product_proccess.bpm.register;

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
@ProcessStarterSubscription(value = LoanVariableConstants.PREFIX_PRODUCT_REGISTER)
public class ProductRegisterStarter extends AbstractProcessStarter<ProductProfileDTO> {

    private final ProductValidationService validationService;
    private final ProductTransactionService transactionService;
    private final ProductBpmService bpmService;

    public ProductRegisterStarter(
            BpmFeignClient bpmFeignClient,
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
    protected String generateAndAttachToBusinessData(StartRequest<ProductProfileDTO> dto) {
        String processInstanceCode = transactionService.getSequence().generateRegistration(LoanVariableConstants.PREFIX_PRODUCT_REGISTER);
        dto.getBusinessData().setProcessInstanceCode(processInstanceCode);
        return processInstanceCode;
    }

    @Override
    protected AttachmentContext createAttachmentContext(ProductProfileDTO businessData) {
        return null;
    }

    @Override
    protected void validateSpecificLogic(ProductProfileDTO businessData) {
        validationService.validateProductInfo(businessData);
    }

    @Override
    protected void saveBusinessData(String processDefinitionKey, ProductProfileDTO businessData) {
        transactionService.createProduct(businessData);

    }

    @Override
    protected ProcessData prepareProcessData(StartRequest<ProductProfileDTO> dto) {
        return bpmService.buildProcessData(dto, LoanVariableConstants.PRODUCT_REGISTER_TXN_CONTENT_CODE);
    }
}
