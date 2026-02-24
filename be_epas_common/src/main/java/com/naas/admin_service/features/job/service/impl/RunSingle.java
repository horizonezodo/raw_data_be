package com.naas.admin_service.features.job.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naas.admin_service.features.job.dto.JobTriggerMessage;
import com.naas.admin_service.features.job.model.ComLogJob;
import com.naas.admin_service.features.job.repository.ComLogJobRepository;
import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class RunSingle {

    private final ComLogJobRepository logJobRepo;
    private final ObjectMapper objectMapper;                 // ✅ inject bean
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final MultitenancyProperties mtProps;

    @Value("${job.kafka.topic.id:JOB-ID}")
    private String jobIdTopic;

    @Transactional
    public void runSingleExecution(Long jobId, String jobName) {
        LocalDateTime now = LocalDateTime.now();

        // 1) insert log RUNNING (đang ở đúng tenant DB nếu TenantContext đã set)
        ComLogJob logJob = new ComLogJob();
        logJob.setJobId(jobId);
        logJob.setJobName(jobName);
        logJob.setStatus("RUNNING");
        logJob.setStartedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        logJob = logJobRepo.save(logJob);

        // 2) build message
        JobTriggerMessage msg = new JobTriggerMessage();
        msg.setLogId(logJob.getId());
        msg.setJobId(jobId);
        msg.setJobName(jobName);
        msg.setStartedAt(logJob.getStartedAt());

        try {
            // ✅ multi=true: bắt buộc có tenant context, vì lib-core sẽ auto add header từ TenantContext
            if (mtProps.isEnabled()) {
                String tenantId = TenantContext.getTenantId();
                if (tenantId == null || tenantId.isBlank()) {
                    throw new IllegalStateException(
                            "multitenancy.enabled=true but TenantContext is empty when producing JOB-ID. " +
                                    "Make sure scheduler sets TenantContext before calling RunSingle."
                    );
                }
            }

            String json = objectMapper.writeValueAsString(msg);

            // ✅ KHÔNG cần ProducerRecord + headers nữa
            // Lib-core ProducerInterceptor sẽ tự add header X-Tenant-Id từ TenantContext
            kafkaTemplate.send(jobIdTopic, jobName, json);

            log.info("Triggered job {} logId={}", jobName, logJob.getId());

        } catch (Exception e) {
            log.error("Failed to send job trigger for {}", jobName, e);
        }
    }
}
