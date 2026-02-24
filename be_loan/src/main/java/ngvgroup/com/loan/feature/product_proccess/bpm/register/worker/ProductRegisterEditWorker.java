package ngvgroup.com.loan.feature.product_proccess.bpm.register.worker;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.product_proccess.dto.ProductProfileDTO;
import ngvgroup.com.loan.feature.product_proccess.service.ProductTransactionService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(LoanVariableConstants.PRODUCT_EXTERNAL_TASK_EDIT)
public class ProductRegisterEditWorker extends AbstractServiceTask {

    private final ProductTransactionService productTransactionService;

    public ProductRegisterEditWorker(ProductTransactionService productTransactionService) {
        this.productTransactionService = productTransactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        ProcessData processDto = CamundaVariablesUtil.getProcessData(externalTask);
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);

        ProductProfileDTO interestData = CamundaVariablesUtil.getTypedVariable(externalTask,
                VariableConstants.BUSINESS_DATA_VARIABLE,
                ProductProfileDTO.class);

        switch (approvalResult) {
            case VariableConstants.SEND_APPROVE:
                if (interestData != null) {
                    productTransactionService.updateProduct(interestData);
                } else {
                    throw new BusinessException(LoanErrorCode.BUSINESS_DATA_MISSING);
                }
                break;

            case VariableConstants.CANCEL:
                productTransactionService.cancelRequest(processDto.getProcessInstanceCode());
                break;

            default:
                throw new BusinessException(LoanErrorCode.INVALID_APPROVAL_STATUS, approvalResult);
        }

        externalTaskService.complete(externalTask);
    }
}
