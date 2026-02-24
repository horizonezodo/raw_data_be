package ngvgroup.com.loan.feature.interest_process.bpm.register;

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
import ngvgroup.com.loan.feature.interest_process.dto.InterestProfileDTO;
import ngvgroup.com.loan.feature.interest_process.service.InterestBpmService;
import ngvgroup.com.loan.feature.interest_process.service.InterestTransactionService;
import ngvgroup.com.loan.feature.interest_process.service.InterestValidationService;
import org.springframework.stereotype.Component;

@Component
@ProcessStarterSubscription(LoanVariableConstants.PREFIX_INTEREST_REGISTER)
public class InterestRegisterStarter extends AbstractProcessStarter<InterestProfileDTO> {
    private final InterestValidationService interestValidationService;
    private final InterestTransactionService interestTransactionService;
    private final InterestBpmService interestBpmService;

    public InterestRegisterStarter(BpmFeignClient bpmFeignClient, FileService fileService, CommonFeignClient commonFeignClient, DocumentTemplateService templateService, InterestValidationService interestValidationService, InterestTransactionService interestTransactionService, InterestBpmService interestBpmService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.interestValidationService = interestValidationService;
        this.interestTransactionService = interestTransactionService;
        this.interestBpmService = interestBpmService;
    }

    @Override
    protected String generateAndAttachToBusinessData(StartRequest<InterestProfileDTO> dto) {
        String processInstanceCode = interestTransactionService.getSequence().generateRegistration(LoanVariableConstants.PREFIX_INTEREST_REGISTER);
        dto.getBusinessData().setProcessInstanceCode(processInstanceCode);
        return processInstanceCode;
    }

    @Override
    protected AttachmentContext createAttachmentContext(InterestProfileDTO businessData) {
        return null;
    }

    @Override
    protected void validateSpecificLogic(InterestProfileDTO businessData) {
        interestValidationService.validateCustomerInfo(businessData);
    }

    @Override
    protected void saveBusinessData(String processDefinitionKey, InterestProfileDTO businessData) {
        interestTransactionService.createInterest(businessData, null);
    }

    @Override
    protected ProcessData prepareProcessData(StartRequest<InterestProfileDTO> dto) {
        return interestBpmService.buildProcessData(dto, LoanVariableConstants.INTEREST_REGISTER_TXN_CONTENT_CODE);
    }
}
