package ngvgroup.com.loan.feature.product_proccess.bpm.adjust.worker;

import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(LoanVariableConstants.PRODUCT_EDIT_EXTERNAL_TASK_APPROVE)
public class ProductAdjustApproveWorker extends AbstractServiceTask {

    protected ProductAdjustApproveWorker() {
        super();
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // Đã xử lý ở BPM
    }
}
