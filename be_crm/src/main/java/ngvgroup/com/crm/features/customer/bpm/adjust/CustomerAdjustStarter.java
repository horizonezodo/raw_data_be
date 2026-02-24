package ngvgroup.com.crm.features.customer.bpm.adjust;

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
import ngvgroup.com.crm.features.common.dto.TemplateResDto;
import ngvgroup.com.crm.features.common.service.CommonService;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;
import ngvgroup.com.crm.features.customer.service.CustomerBpmService;
import ngvgroup.com.crm.features.customer.service.CustomerTransactionService;
import ngvgroup.com.crm.features.customer.service.CustomerValidationService;

// cleaned up unused imports

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
@ProcessStarterSubscription("CRM.201.01")
public class CustomerAdjustStarter extends AbstractProcessStarter<CustomerProfileDTO> {

    private final CustomerValidationService validationService;
    private final CustomerTransactionService transactionService;
    private final CustomerBpmService bpmService;
    private final CommonService commonService;

    public CustomerAdjustStarter(
            BpmFeignClient bpmFeignClient,
            FileService fileService,
            CustomerValidationService validationService,
            CustomerTransactionService transactionService,
            CustomerBpmService bpmService,
            CommonService commonService,
            CommonFeignClient commonFeignClient,
            DocumentTemplateService templateService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.validationService = validationService;
        this.transactionService = transactionService;
        this.bpmService = bpmService;
        this.commonService = commonService;
    }

    @Override
    @Transactional
    protected void saveBusinessData(String processDefinitionKey, CustomerProfileDTO businessData) {
        transactionService.createCustomer(businessData);
    }

    @Override
    protected void validateSpecificLogic(CustomerProfileDTO businessData) {
        validationService.validateCustomerCode(businessData);
        validationService.validateCustomerInfo(businessData);
    }

    @Override
    protected AttachmentContext createAttachmentContext(CustomerProfileDTO businessData) {
        return bpmService.buildAttachmentContext(
                CrmVariableConstants.CUSTOMER_ADJUST_TEMPLATE_FILE_CODE,
                businessData);
    }

    @Override
    protected ProcessData prepareProcessData(StartRequest<CustomerProfileDTO> dto) {
        return bpmService.buildProcessData(dto, CrmVariableConstants.TXN_CONTENT_CODE_ADJUST);
    }

    public byte[] generateTemplateFile(CustomerProfileDTO dto, TemplateResDto template) {
        return bpmService.generateBusinessFile(dto, template);
    }

    public TemplateResDto getTemplateFileDetail() {
        return commonService.getTemplateByCode(CrmVariableConstants.CUSTOMER_ADJUST_TEMPLATE_FILE_CODE);
    }

    @Override
    protected String generateAndAttachToBusinessData(StartRequest<CustomerProfileDTO> dto) {
        String processInstaneCode = transactionService.getSequence().generateCustomerAdjust();

        dto.getBusinessData().getBasicInfo().setProcessInstanceCode(processInstaneCode);

        return processInstaneCode;
    }
}
