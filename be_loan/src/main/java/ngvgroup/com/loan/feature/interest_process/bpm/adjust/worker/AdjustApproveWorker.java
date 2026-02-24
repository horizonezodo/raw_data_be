package ngvgroup.com.loan.feature.interest_process.bpm.adjust.worker;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.TaskBpmData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription(LoanVariableConstants.INTEREST_EDIT_EXTERNAL_TASK_APPROVE)
public class AdjustApproveWorker extends AbstractServiceTask {

    protected AdjustApproveWorker() {
        super();
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        TaskBpmData taskBpmData = CamundaVariablesUtil.getTaskBpmData(externalTask);
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);

        Map<String, Object> variablesToUpdate = new HashMap<>();

        switch (approvalResult) {
            case VariableConstants.APPROVE:
                break;

            case VariableConstants.REJECT:
                variablesToUpdate.putAll(updateMailVariable(taskBpmData));
                break;

            default:
                throw new BusinessException(LoanErrorCode.INVALID_APPROVAL_STATUS, approvalResult);
        }

        externalTaskService.complete(externalTask, variablesToUpdate);
    }

    protected Map<String, Object> updateMailVariable(TaskBpmData taskBpmData) {

        MailVariableDto emailVariables = new MailVariableDto();
        emailVariables.setEmailTemplateCode(LoanVariableConstants.MAIL_CODE_INTEREST_UPDATE);
        emailVariables.setUserNameCc(Collections.singletonList(taskBpmData.getCurrentUser()));
        emailVariables.setUserNameTo(Collections.singletonList(taskBpmData.getCurrentUser()));
        emailVariables.setParamEmail(Map.of());
        taskBpmData.setMailVariable(emailVariables);
        return Map.of(VariableConstants.TASK_BPM_DATA_VARIABLE, taskBpmData);
    }
}