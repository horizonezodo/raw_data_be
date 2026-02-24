package ngvgroup.com.bpm.core.base.listener;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.base.service.TaskService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Slf4j
@Component("taskAssignmentListener")
public class TaskAssignmentListener extends AbstractBpmListener {
    private final TaskService service;

    protected TaskAssignmentListener(IdentityService identityService, TaskService service) {
        super(identityService);
        this.service = service;
    }

    @Override
    protected void handleTask(DelegateTask task) {
        String taskId = task.getId();
        String assignee = task.getAssignee();
        String tenantId = getTenantId(task.getExecution());
        assignee = getStripedName(assignee, tenantId);
        service.afterClaim(taskId, assignee);
    }

    private String getStripedName(String assignee, String tenantId) {
        if (tenantId == null || tenantId.isEmpty())
            return assignee;
        tenantId = tenantId.toLowerCase();
        if (assignee.toLowerCase().endsWith(String.format(".%s", tenantId))) {
            return assignee.replace(String.format(".%s", tenantId), "");
        }
        return assignee;
    }
}
