package ngvgroup.com.fac.feature.sheet_import_export_process.bpm.worker;

import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.sheet_import_export_process.service.SheetTransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(FacVariableConstants.FAC_201_EXTERNAL_TASK_END_PROCESS)
public class SheetTaskEndProcess extends AbstractServiceTask {
    private final SheetTransactionService service;

    public SheetTaskEndProcess(SheetTransactionService service) {
        this.service = service;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        String processInstanceCode = CamundaVariablesUtil.getProcessData(externalTask).getProcessInstanceCode();
        if (VariableConstants.APPROVE.equals(approvalResult)) {
            service.completeTransaction(processInstanceCode);
        }
    }
}
