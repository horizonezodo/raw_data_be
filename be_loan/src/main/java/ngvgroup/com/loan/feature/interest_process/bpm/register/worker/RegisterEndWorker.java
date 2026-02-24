package ngvgroup.com.loan.feature.interest_process.bpm.register.worker;

import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.interest_process.service.InterestTransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(LoanVariableConstants.LOAN_EXTERNAL_TASK_END_PROCESS) // Topic riêng
public class RegisterEndWorker extends AbstractServiceTask {

    private final InterestTransactionService interestTransactionService;

    public RegisterEndWorker(InterestTransactionService interestTransactionService) {
        this.interestTransactionService = interestTransactionService;
    }
    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        ProcessData processData = CamundaVariablesUtil.getProcessData(externalTask);
        if (VariableConstants.APPROVE.equals(approvalResult)) {
            interestTransactionService.updateEndProcess(processData.getProcessInstanceCode(), LoanVariableConstants.PROCESS_REGISTER_TYPE);
        }

        externalTaskService.complete(externalTask);
    }
}
