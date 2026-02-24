package ngvgroup.com.bpm.client.template;

import com.ngvgroup.bpm.core.persistence.config.TenantContextRunner;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import com.ngvgroup.bpm.core.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.bpm.client.utils.CamundaVariablesUtil;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractServiceTask implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            String tenantId = CamundaVariablesUtil.getTenantId(externalTask);
            TenantContextRunner.runWithTenant(tenantId, () -> executeTask(externalTask, externalTaskService));
            externalTaskService.complete(externalTask);
        } catch (BusinessException e) {
            log.error("Business error in approve worker: {}", e.getMessage());
            externalTaskService.handleBpmnError(
                    externalTask,
                    VariableConstants.LOGIC_ERROR,
                    e.getMessage());
        } catch (Exception e) {
            log.error("System error in approve worker: ", e);
            externalTaskService.handleFailure(
                    externalTask,
                    VariableConstants.LOGIC_ERROR,
                    e.getMessage(),
                    0,
                    1);
        }
    }

    protected abstract void executeTask(ExternalTask externalTask, ExternalTaskService externalTaskService);
}
