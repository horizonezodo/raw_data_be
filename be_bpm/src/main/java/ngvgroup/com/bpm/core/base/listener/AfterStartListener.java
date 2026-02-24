package ngvgroup.com.bpm.core.base.listener;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.base.service.ProcessService;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component("afterStartListener")
public class AfterStartListener extends AbstractBpmListener {

    private final ProcessService service;

    protected AfterStartListener(IdentityService identityService, ProcessService service) {
        super(identityService);
        this.service = service;
    }

    @Override
    protected void handleExecution(DelegateExecution execution) {
        log.info("AfterStartListener executed for processInstanceId={}", execution.getProcessInstanceId());
        service.afterStart(execution.getProcessDefinitionId(), execution);
    }

}
