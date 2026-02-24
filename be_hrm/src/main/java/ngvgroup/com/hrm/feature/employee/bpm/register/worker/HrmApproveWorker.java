package ngvgroup.com.hrm.feature.employee.bpm.register.worker;

import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.hrm.core.constant.HrmVariableConstants;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(topicName = HrmVariableConstants.HRM_200_APPROVE_TOPIC)
public class HrmApproveWorker extends AbstractServiceTask {
    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // không làm gì cả
    }
}
