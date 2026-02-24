package ngvgroup.com.fac.feature.sheet_import_export_process.bpm.worker;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.SheetInfoDto;
import ngvgroup.com.fac.feature.sheet_import_export_process.service.SheetTransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(FacVariableConstants.FAC_201_EXTERNAL_TASK_EDIT)
public class SheetTaskEditProcess extends AbstractServiceTask {

    private final SheetTransactionService transactionService;

    public SheetTaskEditProcess(SheetTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        ProcessData processDto = CamundaVariablesUtil.getProcessData(externalTask);
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);

        SheetInfoDto sheetInfoDto = CamundaVariablesUtil.getTypedVariable(externalTask,
                FacVariableConstants.BUSINESS_DATA_VARIABLE,
                SheetInfoDto.class);

        switch (approvalResult) {
            case FacVariableConstants.SEND_APPROVE:
                if (sheetInfoDto != null) {
                    transactionService.executeEditBusiness(sheetInfoDto, processDto.getProcessInstanceCode());
                } else {
                    throw new BusinessException(FacErrorCode.BUSINESS_DATA_MISSING);
                }
                break;

            case FacVariableConstants.CANCEL:
                transactionService.cancelEditBusiness(processDto.getProcessInstanceCode());
                break;

            default:
                throw new BusinessException(FacErrorCode.INVALID_APPROVAL_STATUS, approvalResult);
        }

        externalTaskService.complete(externalTask);
    }
}
