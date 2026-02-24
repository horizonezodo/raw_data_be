package ngvgroup.com.bpm.core.base.listener;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.base.service.TaskService;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Slf4j
@Component("taskCompleteListener")
public class TaskCompleteListener extends AbstractBpmListener {

    private final TaskService service;

    public TaskCompleteListener(IdentityService identityService, TaskService service) {
        super(identityService);
        this.service = service;
    }

    @Override
    protected void handleTask(DelegateTask delegateTask) {
        String taskId = delegateTask.getId(); // ✅ task_id
        String processDefinitionKey = delegateTask.getProcessDefinitionId().split(":")[0];

        log.info("TaskCompleteListener: taskId={}, procInstId={}", taskId, delegateTask.getProcessInstanceId());

        service.afterComplete(taskId, delegateTask, processDefinitionKey);
    }
}
