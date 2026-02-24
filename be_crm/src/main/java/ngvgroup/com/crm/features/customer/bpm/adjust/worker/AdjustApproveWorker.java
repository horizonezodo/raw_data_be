package ngvgroup.com.crm.features.customer.bpm.adjust.worker;

import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(CrmVariableConstants.ADJUST_EXTERNAL_TASK_APPROVE)
public class AdjustApproveWorker extends AbstractServiceTask {

    protected AdjustApproveWorker() {
        super();
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // không làm gì cả
    }
}
