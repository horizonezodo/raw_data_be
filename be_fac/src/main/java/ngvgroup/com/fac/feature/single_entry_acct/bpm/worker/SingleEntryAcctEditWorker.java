package ngvgroup.com.fac.feature.single_entry_acct.bpm.worker;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.template.AbstractServiceTask;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.single_entry_acct.dto.SingleEntryAcctDTO;
import ngvgroup.com.fac.feature.single_entry_acct.service.SingleEntryAcctService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(FacVariableConstants.ENTRY_ACCT_EXTERNAL_TASK_EDIT) // Topic riêng
public class SingleEntryAcctEditWorker extends AbstractServiceTask {

    private final SingleEntryAcctService transactionService;

    public SingleEntryAcctEditWorker(SingleEntryAcctService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    protected void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        ProcessData processDto = CamundaVariablesUtil.getProcessData(externalTask);
        String approvalResult = CamundaVariablesUtil.getApprovalResult(externalTask);
        String processInstanceCode = processDto.getProcessInstanceCode();

        // Lấy dữ liệu customerData từ context Camunda
        SingleEntryAcctDTO singleEntryAcctDTO = CamundaVariablesUtil.getTypedVariable(externalTask,
                VariableConstants.BUSINESS_DATA_VARIABLE,
                SingleEntryAcctDTO.class);

        switch (approvalResult) {
            case VariableConstants.SEND_APPROVE:
                if (singleEntryAcctDTO != null) {
                    transactionService.updateAccountEntry(singleEntryAcctDTO, processInstanceCode);
                } else {
                    throw new BusinessException(FacErrorCode.BUSINESS_DATA_MISSING);
                }
                break;

            case VariableConstants.CANCEL:
                transactionService.cancelRequest(processDto.getProcessInstanceCode());
                break;

            default:
                throw new BusinessException(FacErrorCode.INVALID_APPROVAL_STATUS, approvalResult);
        }

        externalTaskService.complete(externalTask);
    }
}
