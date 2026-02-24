package com.ngvgroup.bpm.core.logging.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.logging.activity.dto.ActivityLogMessage;
import com.ngvgroup.bpm.core.logging.audit.dto.AuditLogMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoggingKafkaProducerService {

    private final KafkaTemplate<String, String> loggingKafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${logging.kafka.topic.activity:activity-log}")
    private String activityLogTopic;

    @Value("${logging.kafka.topic.audit:audit-log}")
    private String auditLogTopic;

    public void sendActivityLog(ActivityLogMessage msg) {
        try {
            String json = objectMapper.writeValueAsString(msg);
            loggingKafkaTemplate.send(activityLogTopic, msg.getRequestId(), json)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Send activity log to Kafka FAILED", ex);
                        } else {
                            log.debug("Send activity log to Kafka OK, topic={}, offset={}",
                                    result.getRecordMetadata().topic(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.error("Cannot serialize ActivityLogMessage", e);
        }
    }

    public void sendAuditLog(AuditLogMessage msg) {
        try {
            String json = objectMapper.writeValueAsString(msg);
            loggingKafkaTemplate.send(auditLogTopic, msg.getRequestId(), json)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Send audit log to Kafka FAILED", ex);
                        } else {
                            log.debug("Send audit log to Kafka OK, topic={}, offset={}",
                                    result.getRecordMetadata().topic(),
                                    result.getRecordMetadata().offset());
                        }
                    });
        } catch (Exception e) {
            log.error("Cannot serialize AuditLogMessage", e);
        }
    }
}