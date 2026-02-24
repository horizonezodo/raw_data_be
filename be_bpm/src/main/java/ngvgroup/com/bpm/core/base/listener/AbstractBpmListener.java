package ngvgroup.com.bpm.core.base.listener;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.config.TenantContextRunner;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.base.dto.TaskBpmData;
import ngvgroup.com.bpm.core.contants.VariableConstants;
import ngvgroup.com.bpm.core.utils.VariablesUtil;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.*;

@Slf4j
public abstract class AbstractBpmListener implements JavaDelegate, ExecutionListener, TaskListener {

    protected final IdentityService identityService;

    protected AbstractBpmListener(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String tenantId = getTenantId(execution);
        TaskBpmData bpmData = VariablesUtil.getData(execution, VariableConstants.TASK_BPM_DATA, TaskBpmData.class);
        String userId = bpmData.getCurrentUser();

        // Vẫn giữ TenantContextRunner nếu đó là thư viện bên ngoài không sửa được
        TenantContextRunner.runWithTenant(tenantId, () -> {
            // SỬ DỤNG TRY-WITH-RESOURCES ĐỂ KHÔNG PHẢI LỒNG THÊM 1 CẤP
            try (var ignored = new AuthenticationScope(userId)) {
                handleExecution(execution);
            } catch (BusinessException e) {
                throw e;
            } catch (Exception e) {
                log.error("Error executing BPM listener", e);
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        });
    }

    @Override
    public void notify(DelegateExecution execution) {
        execute(execution);
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        String tenantId = getTenantId(delegateTask.getExecution());
        TaskBpmData bpmData = VariablesUtil.getData(delegateTask.getExecution(), VariableConstants.TASK_BPM_DATA, TaskBpmData.class);
        String userId = bpmData.getCurrentUser();

        TenantContextRunner.runWithTenant(tenantId, () -> {
            try (var ignored = new AuthenticationScope(userId)) {
                handleTask(delegateTask);
            }
        });
    }

    protected void handleExecution(DelegateExecution execution) {
        // Default empty implementation
    }

    protected void handleTask(DelegateTask task) {
        // Default empty implementation
    }

    protected String getTenantId(VariableScope scope) {
        Object tenantIdObj = scope.getVariable(VariableConstants.TENANT_ID);
        return tenantIdObj != null ? tenantIdObj.toString() : null;
    }

    /**
     * Helper Class: Tự động set Authentication khi khởi tạo và clear khi đóng (close).
     */
    protected class AuthenticationScope implements AutoCloseable {
        private boolean authSet = false;

        public AuthenticationScope(String userId) {
            if (userId != null && !userId.isBlank()) {
                identityService.setAuthenticatedUserId(userId);
                this.authSet = true;
            }
        }

        @Override
        public void close() {
            if (authSet) {
                identityService.clearAuthentication();
            }
        }
    }
}