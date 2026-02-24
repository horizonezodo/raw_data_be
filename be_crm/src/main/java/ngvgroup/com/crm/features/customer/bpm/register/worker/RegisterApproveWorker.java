package ngvgroup.com.crm.features.customer.bpm.register.worker;

import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(CrmVariableConstants.REGISTER_EXTERNAL_TASK_APPROVE)
public class RegisterApproveWorker extends AbstractServiceTask {

    protected RegisterApproveWorker() {
        super();
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // không làm gì cả
    }
}
