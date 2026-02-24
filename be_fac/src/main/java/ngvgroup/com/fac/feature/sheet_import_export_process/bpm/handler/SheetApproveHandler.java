package ngvgroup.com.fac.feature.sheet_import_export_process.bpm.handler;

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
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.SheetInfoDto;
import ngvgroup.com.fac.feature.sheet_import_export_process.service.impl.SheetBpmServiceImpl;
import org.springframework.stereotype.Component;

@Component
@UserTaskSubscription("FAC.201.01.01")
public class SheetApproveHandler extends AbstractUserTask<SheetInfoDto> {
    private final SheetBpmServiceImpl sheetBpmService;

    public SheetApproveHandler(BpmFeignClient bpmFeignClient,
                               FileService fileService,
                               CommonFeignClient commonFeignClient,
                               DocumentTemplateService templateService,
                               SheetBpmServiceImpl sheetBpmService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.sheetBpmService = sheetBpmService;
    }

    @Override
    protected void saveDraftBusinessData(SheetInfoDto businessData) {
        // Bước phê duyệt thường không lưu/sửa data business
    }

    @Override
    protected void validateSpecificLogic(SheetInfoDto businessData) {
        // Bước phê duyệt thường không validate lại data
    }

    @Override
    protected String getProcessTypeCode() {
        return FacVariableConstants.PREFIX_SHEET;
    }

    @Override
    protected AttachmentContext createAttachmentContext(SheetInfoDto businessData) {
        return null;
    }

    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<SheetInfoDto> dto) {
        return sheetBpmService.buildMailVariable(FacVariableConstants.MAIL_TEMPLATE_CODE, dto);
    }

    @Override
    protected SheetInfoDto getBusinessData(TaskViewBpmData bpmData) {
        return sheetBpmService.getApprovalDetail(bpmData.getProcessInstanceCode());
    }
}
