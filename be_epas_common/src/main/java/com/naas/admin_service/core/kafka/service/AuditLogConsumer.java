package com.naas.admin_service.core.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naas.admin_service.features.log.service.ComInfLogAuditService;
import com.ngvgroup.bpm.core.logging.audit.dto.AuditLogMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditLogConsumer {

    private final ComInfLogAuditService persistService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(
            topics = "${logging.kafka.topic.audit:COM_INF_LOG_AUDIT_TOPIC}",
            groupId = "${logging.kafka.consumer-group:activity-log-consumer}"
    )
    public void consume(String messageJson) {
        try {
            AuditLogMessage msg =
                    objectMapper.readValue(messageJson, AuditLogMessage.class);
            persistService.save(msg);
            log.debug("Saved audit log requestId={}, table={}, recordId={}",
                    msg.getRequestId(), msg.getTableName(), msg.getRecordId());
        } catch (Exception e) {
            log.error("Failed to consume audit log JSON: {}", messageJson, e);
        }
    }
}