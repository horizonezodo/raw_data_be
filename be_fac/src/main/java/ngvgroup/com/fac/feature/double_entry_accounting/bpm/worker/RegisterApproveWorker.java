package ngvgroup.com.fac.feature.double_entry_accounting.bpm.worker;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryTransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription(FacVariableConstants.EXTERNAL_TASK_APPROVE_DOUBLE_ENTRY_ACCT)
public class RegisterApproveWorker extends AbstractServiceTask {
    private final DoubleEntryTransactionService doubleEntryTransactionService;
    public RegisterApproveWorker(DoubleEntryTransactionService doubleEntryTransactionService) {

        this.doubleEntryTransactionService=doubleEntryTransactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService){
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        ProcessData processData = CamundaVariablesUtil.getProcessData(externalTask);
        Map<String, Object> variablesToUpdate = new HashMap<>();

        switch (approvalResult) {
            case FacVariableConstants.APPROVE:
                doubleEntryTransactionService.updateBusinessStatus(processData.getProcessInstanceCode(),FacVariableConstants.COMPLETE);
                break;

            case FacVariableConstants.REJECT:
                doubleEntryTransactionService.updateBusinessStatus(processData.getProcessInstanceCode(),approvalResult);

                break;

            default:
                throw new BusinessException(FacErrorCode.INVALID_APPROVAL_STATUS, approvalResult);
        }


        externalTaskService.complete(externalTask, variablesToUpdate);
    }
}
