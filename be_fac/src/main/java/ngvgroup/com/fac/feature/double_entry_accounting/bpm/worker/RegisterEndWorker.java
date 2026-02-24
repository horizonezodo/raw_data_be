package ngvgroup.com.fac.feature.double_entry_accounting.bpm.worker;

import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryTransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(FacVariableConstants.EXTERNAL_TASK_END_PROCESS_DOUBLE_ENTRY_PROCESS)
public class RegisterEndWorker extends AbstractServiceTask {
    private final DoubleEntryTransactionService doubleEntryTransactionService;

    public RegisterEndWorker(DoubleEntryTransactionService doubleEntryTransactionService) {
        this.doubleEntryTransactionService = doubleEntryTransactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService){
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        ProcessData processData = CamundaVariablesUtil.getProcessData(externalTask);

        if (FacVariableConstants.APPROVE.equals(approvalResult)) {
            doubleEntryTransactionService.updateDoubleEntryAcctEndProcess(processData.getProcessInstanceCode());
        }
        externalTaskService.complete(externalTask);
    }
}
