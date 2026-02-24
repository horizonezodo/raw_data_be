package ngvgroup.com.loan.feature.interest_process.bpm.adjust.worker;

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
@ExternalTaskSubscription(LoanVariableConstants.INTEREST_EDIT_EXTERNAL_TASK_END_PROCESS)
public class AdjustEndWorker extends AbstractServiceTask {

    private final InterestTransactionService interestTransactionService;

    public AdjustEndWorker(InterestTransactionService interestTransactionService) {
        this.interestTransactionService = interestTransactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        ProcessData processData = CamundaVariablesUtil.getProcessData(externalTask);

        // Nếu trạng thái cuối cùng là APPROVE thì mới chốt dữ liệu sang bảng
        // INF/History
        if (VariableConstants.APPROVE.equals(approvalResult)) {
            interestTransactionService.updateEndProcess(processData.getProcessInstanceCode(), LoanVariableConstants.PROCESS_EDIT_TYPE);
        }

        externalTaskService.complete(externalTask);
    }
}
