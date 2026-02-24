package ngvgroup.com.bpm.core.base.listener;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.base.service.TaskService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Slf4j
@Component("taskCreateListener")
public class TaskCreateListener extends AbstractBpmListener {

    private final TaskService service;

    protected TaskCreateListener(IdentityService identityService, TaskService service) {
        super(identityService);
        this.service = service;
    }

    @Override
    protected void handleTask(DelegateTask task) {
        // chạy đúng lúc task vừa được tạo
        log.info("TASK_CREATED: id={}, defKey={}, name={}, assignee={}, piId={}, pdId={}",
                task.getId(),
                task.getTaskDefinitionKey(),
                task.getName(),
                task.getAssignee(),
                task.getProcessInstanceId(),
                task.getProcessDefinitionId());
        service.onTaskCreate(task);
    }
}
