package ngvgroup.com.bpm.core.base.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.core.base.dto.TaskBpmData;
import ngvgroup.com.bpm.core.contants.VariableConstants;
import ngvgroup.com.bpm.core.utils.VariablesUtil;

@Slf4j
@Component("sendEmailListener")
public class SendEmailListener extends AbstractBpmListener {

    @Value("${kafka.topic.email}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    protected SendEmailListener(IdentityService identityService, KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        super(identityService);
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handleExecution(DelegateExecution execution) {
        log.info("SendEmailListener executed for processInstanceId={}", execution.getProcessInstanceId());

        TaskBpmData taskBpmData = VariablesUtil.getData(execution, VariableConstants.TASK_BPM_DATA, TaskBpmData.class);

        if (taskBpmData != null && taskBpmData.getMailVariable() != null) {
            try {
                kafkaTemplate.send(topic, objectMapper.writeValueAsString(taskBpmData.getMailVariable()));
            } catch (JsonProcessingException e) {
                log.error("Error serializing mail request", e);
                throw new BusinessException(ErrorCode.BAD_REQUEST);
            }
        } else {
            log.warn("No MailVariableDto found in TaskBpmData for processInstanceId={}",
                    execution.getProcessInstanceId());
        }

    }
}