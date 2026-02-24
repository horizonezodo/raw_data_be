package ngvgroup.com.fac.feature.double_entry_accounting.bpm.worker;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.double_entry_accounting.dto.DoubleEntryAccountingProcessDto;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryTransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription(FacVariableConstants.EXTERNAL_TASK_EDIT_DOUBLE_ENTRY_ACCT)
public class RegisterEditWorker extends AbstractServiceTask {
    private final DoubleEntryTransactionService doubleEntryTransactionService;
    public RegisterEditWorker(DoubleEntryTransactionService doubleEntryTransactionService) {

        this.doubleEntryTransactionService=doubleEntryTransactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService){
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        ProcessData processData = CamundaVariablesUtil.getProcessData(externalTask);
        Map<String, Object> variablesToUpdate = new HashMap<>();

        DoubleEntryAccountingProcessDto doubleEntryAccountingProcessReqDto = CamundaVariablesUtil.getTypedVariable(externalTask,
                FacVariableConstants.BUSINESS_DATA_VARIABLE,
                DoubleEntryAccountingProcessDto.class);
        switch (approvalResult) {
            case FacVariableConstants.SEND_APPROVE:
                if (doubleEntryAccountingProcessReqDto != null) {
                    doubleEntryTransactionService.updateDoubleEntry(doubleEntryAccountingProcessReqDto,processData.getProcessInstanceCode());
                    doubleEntryTransactionService.updateBusinessStatus(processData.getProcessInstanceCode(),FacVariableConstants.ACTIVE);
                } else {
                    throw new BusinessException(FacErrorCode.BUSINESS_DATA_MISSING);
                }
                break;

            case FacVariableConstants.CANCEL:
                doubleEntryTransactionService.updateBusinessStatus(processData.getProcessInstanceCode(),approvalResult);

                break;

            default:
                throw new BusinessException(FacErrorCode.INVALID_APPROVAL_STATUS, approvalResult);
        }
        externalTaskService.complete(externalTask, variablesToUpdate);
    }
}
