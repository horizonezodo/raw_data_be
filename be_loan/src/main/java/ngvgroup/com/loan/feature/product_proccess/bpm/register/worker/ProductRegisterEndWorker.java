package ngvgroup.com.loan.feature.product_proccess.bpm.register.worker;

import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.product_proccess.service.ProductTransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(LoanVariableConstants.PRODUCT_EXTERNAL_TASK_END_PROCESS)
public class ProductRegisterEndWorker extends AbstractServiceTask {

    private final ProductTransactionService transactionService;

    public ProductRegisterEndWorker(ProductTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        ProcessData processData = CamundaVariablesUtil.getProcessData(externalTask);
        if (VariableConstants.APPROVE.equals(approvalResult)) {
            transactionService.updateEndProcess(processData.getProcessInstanceCode(), LoanVariableConstants.PROCESS_REGISTER_TYPE);
        }

        externalTaskService.complete(externalTask);
    }
}
