package ngvgroup.com.bpm.core.base.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;

public interface ProcessService {
    void afterStart(String processDefinitionKey, DelegateExecution execution);

    void beforeEnd(String endStatus, DelegateExecution execution);
}
