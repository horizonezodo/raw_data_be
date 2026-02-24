package ngvgroup.com.fac.feature.sheet_import_export_process.bpm;

import ngvgroup.com.bpm.client.annotation.ProcessStarterSubscription;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.shared.AttachmentContext;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.feign.CommonFeignClient;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.bpm.client.service.FileService;
import ngvgroup.com.bpm.client.template.AbstractProcessStarter;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.SheetInfoDto;
import ngvgroup.com.fac.feature.sheet_import_export_process.service.SheetBpmService;
import ngvgroup.com.fac.feature.sheet_import_export_process.service.SheetTransactionService;
import ngvgroup.com.fac.feature.sheet_import_export_process.service.SheetValidationService;
import org.springframework.stereotype.Component;

@Component
@ProcessStarterSubscription("FAC.201.01")
public class SheetRegisterStarter extends AbstractProcessStarter<SheetInfoDto> {
    private final SheetValidationService validationService;
    private final SheetTransactionService transactionService;
    private final SheetBpmService sheetBpmService;

    public SheetRegisterStarter(BpmFeignClient bpmFeignClient, FileService fileService, SheetValidationService validationService, SheetTransactionService transactionService, CommonFeignClient commonFeignClient, DocumentTemplateService templateService,

                                SheetBpmService sheetBpmService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.validationService = validationService;
        this.transactionService = transactionService;
        this.sheetBpmService = sheetBpmService;
    }

    @Override
    protected String generateAndAttachToBusinessData(StartRequest<SheetInfoDto> dto) {
        String processInstanceCode = transactionService.generateSeq(dto.getBusinessData().getOrgCode());
        dto.getBusinessData().setProcessInstanceCode(processInstanceCode);
        return processInstanceCode;
    }

    @Override
    protected AttachmentContext createAttachmentContext(SheetInfoDto businessData) {
        return null;
    }

    @Override
    protected void validateSpecificLogic(SheetInfoDto businessData) {
        validationService.validateSheetInfo(businessData);
    }

    @Override
    protected void saveBusinessData(String processDefinitionKey, SheetInfoDto businessData) {
        transactionService.startProcess(businessData);
    }

    @Override
    protected ProcessData prepareProcessData(StartRequest<SheetInfoDto> dto) {
        return sheetBpmService.buildProcessData(dto);
    }

}
