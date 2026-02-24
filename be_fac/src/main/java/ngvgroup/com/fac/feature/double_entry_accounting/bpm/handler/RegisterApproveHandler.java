package ngvgroup.com.fac.feature.double_entry_accounting.bpm.handler;

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
import ngvgroup.com.fac.feature.double_entry_accounting.dto.DoubleEntryAccountingProcessDto;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryBpmService;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryTransactionService;
import org.springframework.stereotype.Component;

@Component
@UserTaskSubscription(FacVariableConstants.DOUBLE_ENTRY_ACCT_APPROVE)
public class RegisterApproveHandler extends AbstractUserTask<DoubleEntryAccountingProcessDto> {
    private final DoubleEntryTransactionService doubleEntryTransactionService;
    private final DoubleEntryBpmService doubleEntryBpmService;

    public RegisterApproveHandler(
            BpmFeignClient bpmFeignClient,
            FileService fileService,
            DoubleEntryTransactionService doubleEntryTransactionService,
            DoubleEntryBpmService doubleEntryBpmService,
            CommonFeignClient commonFeignClient,
            DocumentTemplateService templateService) {
        super(bpmFeignClient, fileService, commonFeignClient, templateService);
        this.doubleEntryTransactionService = doubleEntryTransactionService;
        this.doubleEntryBpmService = doubleEntryBpmService;
    }

    @Override
    protected void saveDraftBusinessData(DoubleEntryAccountingProcessDto businessData) {
        // Bước phê duyệt thường không lưu/sửa data business
    }

    @Override
    protected void validateSpecificLogic(DoubleEntryAccountingProcessDto businessData) {
        // Bước phê duyệt thường không validate lại data
    }

    @Override
    protected String getProcessTypeCode() {
        return "";
    }

    @Override
    protected AttachmentContext createAttachmentContext(DoubleEntryAccountingProcessDto businessData) {
        return null;
    }

    @Override
    protected MailVariableDto prepareMailVariable(SubmitTaskRequest<DoubleEntryAccountingProcessDto> dto) {
        return doubleEntryBpmService.buildMailVariable(FacVariableConstants.DOUBLE_ACCT_PROCESS_TYPE_CODE, dto);
    }

    @Override
    protected DoubleEntryAccountingProcessDto getBusinessData(TaskViewBpmData bpmData) {
        return doubleEntryTransactionService.getDetail(bpmData.getProcessInstanceCode());
    }
}
