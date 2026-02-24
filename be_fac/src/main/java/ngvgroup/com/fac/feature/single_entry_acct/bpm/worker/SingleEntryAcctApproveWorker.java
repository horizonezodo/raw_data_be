package ngvgroup.com.fac.feature.single_entry_acct.bpm.worker;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(FacVariableConstants.ENTRY_ACCT_EXTERNAL_TASK_APPROVE)
public class SingleEntryAcctApproveWorker extends AbstractServiceTask {

    protected SingleEntryAcctApproveWorker() {
        super();
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);

        switch (approvalResult) {
            case VariableConstants.APPROVE:
                break;

            case VariableConstants.REJECT:
                break;

            default:
                throw new BusinessException(FacErrorCode.INVALID_APPROVAL_STATUS, approvalResult);
        }
    }
}
