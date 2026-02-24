package ngvgroup.com.crm.features.customer.bpm.adjust.worker;

import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.crm.features.customer.service.CustomerTransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(CrmVariableConstants.ADJUST_EXTERNAL_TASK_END_PROCESS)
public class AdjustEndWorker extends AbstractServiceTask {

    private final CustomerTransactionService customerTransactionService;

    public AdjustEndWorker(CustomerTransactionService customerTransactionService) {
        this.customerTransactionService = customerTransactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        ProcessData processData = CamundaVariablesUtil.getProcessData(externalTask);

        if (VariableConstants.APPROVE.equals(approvalResult)) {
            customerTransactionService.updateEndProcess(processData.getProcessInstanceCode());
        }

        externalTaskService.complete(externalTask);
    }
}
