package ngvgroup.com.crm.features.customer.bpm.register;

import ngvgroup.com.bpm.client.annotation.ProcessStarterSubscription;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.feign.CommonFeignClient;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.template.AbstractProcessStarter;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;
import ngvgroup.com.crm.features.customer.service.CustomerBpmService;
import ngvgroup.com.crm.features.customer.service.CustomerTransactionService;
import ngvgroup.com.crm.features.customer.service.CustomerValidationService;

// cleaned up unused imports

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
@ProcessStarterSubscription("CRM.200.01")
public class CustomerRegisterStarter extends AbstractProcessStarter<CustomerProfileDTO> {

    private final CustomerValidationService validationService;
    private final CustomerTransactionService transactionService;
    private final CustomerBpmService bpmService;

    public CustomerRegisterStarter(
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
    protected void saveBusinessData(String processDefinitionKey, CustomerProfileDTO businessData) {
        // 2. Sinh Mã Khách Hàng (Logic riêng của Register)

        // 4. Tạo Transaction (Snapshot ban đầu)
        transactionService.createCustomer(businessData);
    }

    @Override
    protected void validateSpecificLogic(CustomerProfileDTO businessData) {
        validationService.validateCustomerInfo(businessData);
    }

    @Override
    protected AttachmentContext createAttachmentContext(CustomerProfileDTO businessData) {
        return bpmService.buildAttachmentContext(
                CrmVariableConstants.CUSTOMER_REGISTER_TEMPLATE_FILE_CODE,
                businessData);
    }

    @Override
    protected ProcessData prepareProcessData(StartRequest<CustomerProfileDTO> dto) {
        return bpmService.buildProcessData(dto, CrmVariableConstants.TXN_CONTENT_CODE_REGISTER);
    }

    @Override
    protected String generateAndAttachToBusinessData(StartRequest<CustomerProfileDTO> dto) {
        String customerCode = transactionService.getSequence()
                .generateMaKhachHang(dto.getBusinessData().getBasicInfo().getOrgCode());
        String processInstanceCode = transactionService.getSequence().generateCustomerRegistration();

        // 3. Set vào DTO
        dto.getBusinessData().getBasicInfo().setProcessInstanceCode(processInstanceCode);
        dto.getBusinessData().getBasicInfo().setCustomerCode(customerCode);
        return processInstanceCode;
    }
}
