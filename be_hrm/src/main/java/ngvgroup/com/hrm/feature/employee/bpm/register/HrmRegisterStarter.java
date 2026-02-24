package ngvgroup.com.hrm.feature.employee.bpm.register;

import ngvgroup.com.bpm.client.annotation.ProcessStarterSubscription;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.feign.CommonFeignClient;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.template.AbstractProcessStarter;
import ngvgroup.com.hrm.core.constant.HrmVariableConstants;
import ngvgroup.com.hrm.feature.employee.dto.HrmProfileDto;
import ngvgroup.com.hrm.feature.employee.service.BpmService;
import ngvgroup.com.hrm.feature.employee.service.TransactionService;
import ngvgroup.com.hrm.feature.employee.service.ValidationSerivce;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ProcessStarterSubscription("HRM.200.01")
public class HrmRegisterStarter extends AbstractProcessStarter<HrmProfileDto> {

    private final TransactionService transactionService;
    private final ValidationSerivce validationSerivce;
    private final BpmService bpmService;

    public HrmRegisterStarter(BpmFeignClient bpmFeignClient, FileService fileService,
            CommonFeignClient commonFeignClient, DocumentTemplateService templateService,
            TransactionService transactionService, ValidationSerivce validationSerivce, BpmService bpmService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.transactionService = transactionService;
        this.validationSerivce = validationSerivce;
        this.bpmService = bpmService;
    }

    @Override
    protected String generateAndAttachToBusinessData(StartRequest<HrmProfileDto> dto) {
        String processInstanceCode = transactionService.getSequence().generateRegisterProcessInstanceCode();
        String employeeCode = transactionService.getSequence().generateEmployeeCode();

        dto.getBusinessData().getBasicInfo().setProcessInstanceCode(processInstanceCode);
        dto.getBusinessData().getBasicInfo().setEmployeeCode(employeeCode);

        return processInstanceCode;
    }

    @Override
    protected AttachmentContext createAttachmentContext(HrmProfileDto businessData) {
        return null;
    }

    @Override
    protected void validateSpecificLogic(HrmProfileDto businessData) {
        validationSerivce.validateHrmProfile(businessData);
    }

    @Override
    @Transactional
    protected void saveBusinessData(String processDefinitionKey, HrmProfileDto businessData) {
        transactionService.createEmployee(processDefinitionKey, businessData);
    }

    @Override
    protected ProcessData prepareProcessData(StartRequest<HrmProfileDto> dto) {
        return bpmService.buildProcessData(dto, HrmVariableConstants.TXN_CONTENT_CODE_HRM_REGISTER);
    }
}
