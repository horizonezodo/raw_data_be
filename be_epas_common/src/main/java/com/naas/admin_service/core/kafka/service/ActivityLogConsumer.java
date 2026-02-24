package com.naas.admin_service.core.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naas.admin_service.features.log.service.ComInfLogActivityService;
import com.ngvgroup.bpm.core.logging.activity.dto.ActivityLogMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityLogConsumer {

    private final ComInfLogActivityService persistService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(
            topics = "${logging.kafka.topic.activity:activity-log}",
            groupId = "${logging.kafka.consumer-group:activity-log-consumer}"
    )
    public void consume(String messageJson) {
        try {
            ActivityLogMessage msg =
                    objectMapper.readValue(messageJson, ActivityLogMessage.class);
            persistService.save(msg);
            log.debug("Saved activity log requestId={}, url={}", msg.getRequestId(), msg.getRequestUrl());
        } catch (Exception e) {
            log.error("Failed to consume activity log JSON: {}", messageJson, e);
        }
    }
}