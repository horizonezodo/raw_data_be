package ngvgroup.com.hrm.feature.employee.bpm.register.worker;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.hrm.core.constant.HrmErrorCode;
import ngvgroup.com.hrm.core.constant.HrmVariableConstants;
import ngvgroup.com.hrm.feature.employee.dto.HrmProfileDto;
import ngvgroup.com.hrm.feature.employee.service.TransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(topicName = HrmVariableConstants.HRM_200_EDIT_TOPIC)
public class HrmEditWorker extends AbstractServiceTask {

    private final TransactionService transactionService;

    public HrmEditWorker(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        ProcessData processDto = CamundaVariablesUtil.getProcessData(externalTask);
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);

        // Lấy dữ liệu customerData từ context Camunda
        HrmProfileDto customerData = CamundaVariablesUtil.getTypedVariable(externalTask,
                VariableConstants.BUSINESS_DATA_VARIABLE,
                HrmProfileDto.class);

        switch (approvalResult) {
            case VariableConstants.SEND_APPROVE:
                if (customerData != null) {
                    transactionService.updateEmployee(processDto.getProcessInstanceCode(), customerData);
                } else {
                    throw new BusinessException(HrmErrorCode.BUSINESS_DATA_MISSING);
                }
                break;

            case VariableConstants.CANCEL:
                transactionService.cancelRequest(processDto.getProcessInstanceCode());
                break;

            default:
                throw new BusinessException(HrmErrorCode.INVALID_APPROVAL_STATUS, approvalResult);
        }
    }
}
