package ngvgroup.com.hrm.feature.employee.bpm.register.worker;

import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.hrm.core.constant.HrmVariableConstants;
import ngvgroup.com.hrm.feature.employee.service.TransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(topicName = HrmVariableConstants.HRM_200_END_PROCESS_TOPIC)
public class HrmEndProcessWorker extends AbstractServiceTask {

    private final TransactionService transactionService;

    public HrmEndProcessWorker(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        ProcessData processData = CamundaVariablesUtil.getProcessData(externalTask);

        if (VariableConstants.APPROVE.equals(approvalResult)) {
            transactionService.updateEndProcess(processData.getProcessInstanceCode());
        }
    }
}
