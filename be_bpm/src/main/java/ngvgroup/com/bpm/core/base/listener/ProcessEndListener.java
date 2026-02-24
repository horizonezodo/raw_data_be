package ngvgroup.com.bpm.core.base.listener;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.base.service.ProcessService;
import ngvgroup.com.bpm.core.contants.VariableConstants;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component("processEndListener")
public class ProcessEndListener extends AbstractBpmListener {

    private final ProcessService service;

    protected ProcessEndListener(IdentityService identityService, ProcessService service) {
        super(identityService);
        this.service = service;
    }

    @Override
    protected void handleExecution(DelegateExecution execution) {
        log.info("ProcessEndListener executed for processInstanceId={}", execution.getProcessInstanceId());

        String status = execution.getVariable(VariableConstants.APPROVAL_RESULT).toString();

        service.beforeEnd(status, execution);
    }
}
