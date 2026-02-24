package ngvgroup.com.loan.feature.interest_process.bpm.register.worker;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.interest_process.dto.InterestProfileDTO;
import ngvgroup.com.loan.feature.interest_process.service.InterestTransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(LoanVariableConstants.LOAN_EXTERNAL_TASK_EDIT) // Topic riêng
public class RegisterEditWorker extends AbstractServiceTask {
    private final InterestTransactionService interestTransactionService;

    public RegisterEditWorker(InterestTransactionService interestTransactionService) {
        this.interestTransactionService = interestTransactionService;
    }
    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        ProcessData processDto = CamundaVariablesUtil.getProcessData(externalTask);
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);

        // Lấy dữ liệu customerData từ context Camunda
        InterestProfileDTO interestData = CamundaVariablesUtil.getTypedVariable(externalTask,
                VariableConstants.BUSINESS_DATA_VARIABLE,
                InterestProfileDTO.class);

        switch (approvalResult) {
            case VariableConstants.SEND_APPROVE:
                if (interestData != null) {
                    interestTransactionService.updateInterest(interestData);
                } else {
                    throw new BusinessException(LoanErrorCode.BUSINESS_DATA_MISSING);
                }
                break;

            case VariableConstants.CANCEL:
                interestTransactionService.cancelRequest(processDto.getProcessInstanceCode());
                break;

            default:
                throw new BusinessException(LoanErrorCode.INVALID_APPROVAL_STATUS, approvalResult);
        }

        externalTaskService.complete(externalTask);
    }
}
