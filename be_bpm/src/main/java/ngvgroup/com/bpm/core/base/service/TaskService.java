package ngvgroup.com.bpm.core.base.service;

import org.camunda.bpm.engine.delegate.DelegateTask;

public interface TaskService {
    void onTaskCreate(DelegateTask task);

    void afterClaim(String taskId, String assignee);

    void afterComplete(String taskId, DelegateTask delegateTask, String processTypeCode);
}
