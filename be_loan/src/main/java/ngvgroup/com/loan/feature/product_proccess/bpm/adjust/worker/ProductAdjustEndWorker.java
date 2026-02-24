package ngvgroup.com.loan.feature.product_proccess.bpm.adjust.worker;

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
@ExternalTaskSubscription(LoanVariableConstants.PRODUCT_EDIT_EXTERNAL_TASK_END_PROCESS)
public class ProductAdjustEndWorker extends AbstractServiceTask {

    private final ProductTransactionService productTransactionService;

    public ProductAdjustEndWorker(ProductTransactionService productTransactionService) {
        this.productTransactionService = productTransactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        ProcessData processData = CamundaVariablesUtil.getProcessData(externalTask);

        if (VariableConstants.APPROVE.equals(approvalResult)) {
            productTransactionService.updateEndProcess(
                    processData.getProcessInstanceCode(),
                    LoanVariableConstants.PROCESS_EDIT_TYPE
            );
        }

        externalTaskService.complete(externalTask);
    }
}
