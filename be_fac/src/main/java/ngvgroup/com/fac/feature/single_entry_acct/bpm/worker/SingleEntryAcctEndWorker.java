package ngvgroup.com.fac.feature.single_entry_acct.bpm.worker;

import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.single_entry_acct.service.SingleEntryAcctService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(FacVariableConstants.ENTRY_ACCT_EXTERNAL_TASK_END_PROCESS) // Topic riêng
public class SingleEntryAcctEndWorker extends AbstractServiceTask {

    private final SingleEntryAcctService transactionService;

    public SingleEntryAcctEndWorker(SingleEntryAcctService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);

        String processInstanceCode = CamundaVariablesUtil.getProcessData(externalTask).getProcessInstanceCode();
        if (ngvgroup.com.bpm.client.constant.VariableConstants.APPROVE.equals(approvalResult)) {
            transactionService.updateEndProcess(processInstanceCode);
        }
    }
}
