package com.naas.admin_service.core.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naas.admin_service.features.job.dto.JobStatusMessage;
import com.naas.admin_service.features.job.model.ComLogJob;
import com.naas.admin_service.features.job.repository.ComLogJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobStatusConsumer {

    private final ComLogJobRepository logJobRepo;
    private final ObjectMapper objectMapper; // ✅ inject bean

    @KafkaListener(
            topics = "${job.kafka.topic.status:JOB-STATUS}",
            groupId = "${job.kafka.consumer-group.status:ADMIN_JOB_STATUS_GROUP}"
    )
    @Transactional
    public void consume(String messageJson) {
        try {
            // ✅ TenantContext đã được lib-core set sẵn từ Kafka header (multi=true)
            JobStatusMessage msg = objectMapper.readValue(messageJson, JobStatusMessage.class);

            Optional<ComLogJob> opt = logJobRepo.findById(msg.getLogId());
            if (opt.isEmpty()) {
                log.warn("Job log not found for id={}, jobName={}", msg.getLogId(), msg.getJobName());
                return;
            }

            ComLogJob logJob = opt.get();
            logJob.setStatus(msg.getStatus());
            logJob.setFinishedAt(msg.getFinishedAt());
            logJob.setResultMessage(msg.getResultMessage());
            logJobRepo.save(logJob);

            log.info("Updated job log id={} status={}", msg.getLogId(), msg.getStatus());

        } catch (Exception e) {
            log.error("Failed to consume job status JSON: {}", messageJson, e);
        }
    }
}
