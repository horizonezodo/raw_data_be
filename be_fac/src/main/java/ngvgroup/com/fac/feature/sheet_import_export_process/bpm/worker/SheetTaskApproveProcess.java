package ngvgroup.com.fac.feature.sheet_import_export_process.bpm.worker;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(FacVariableConstants.FAC_201_EXTERNAL_TASK_APPROVE)
public class SheetTaskApproveProcess extends AbstractServiceTask {

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        switch (approvalResult) {
            case ngvgroup.com.bpm.client.constant.VariableConstants.APPROVE:
                break;

            case ngvgroup.com.bpm.client.constant.VariableConstants.REJECT:
                break;

            default:
                throw new BusinessException(FacErrorCode.INVALID_APPROVAL_STATUS, approvalResult);
        }

    }
}
