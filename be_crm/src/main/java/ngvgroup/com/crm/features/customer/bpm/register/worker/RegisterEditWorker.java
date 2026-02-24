package ngvgroup.com.crm.features.customer.bpm.register.worker;

import com.ngvgroup.bpm.core.common.exception.BusinessException;

import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.crm.core.constant.CrmErrorCode;
import ngvgroup.com.crm.core.constant.CrmVariableConstants;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;
import ngvgroup.com.crm.features.customer.service.CustomerTransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(CrmVariableConstants.REGISTER_EXTERNAL_TASK_EDIT) // Topic riêng
public class RegisterEditWorker extends AbstractServiceTask {

    private final CustomerTransactionService customerTransactionService;

    public RegisterEditWorker(CustomerTransactionService customerTransactionService) {
        this.customerTransactionService = customerTransactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        ProcessData processDto = CamundaVariablesUtil.getProcessData(externalTask);
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);

        // Lấy dữ liệu customerData từ context Camunda
        CustomerProfileDTO customerData = CamundaVariablesUtil.getTypedVariable(externalTask,
                VariableConstants.BUSINESS_DATA_VARIABLE,
                CustomerProfileDTO.class);

        switch (approvalResult) {
            case VariableConstants.SEND_APPROVE:
                if (customerData != null) {
                    customerTransactionService.updateCustomer(processDto.getProcessInstanceCode(), customerData);
                } else {
                    throw new BusinessException(CrmErrorCode.BUSINESS_DATA_MISSING);
                }
                break;

            case VariableConstants.CANCEL:
                customerTransactionService.cancelRequest(processDto.getProcessInstanceCode());
                break;

            default:
                throw new BusinessException(CrmErrorCode.INVALID_APPROVAL_STATUS, approvalResult);
        }

        externalTaskService.complete(externalTask);
    }
}
