package com.naas.admin_service.core.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.job.dto.JobStatusMessage;
import com.naas.admin_service.features.job.dto.JobTriggerMessage;
import com.naas.admin_service.features.log.service.ComInfLogActivityService;
import com.naas.admin_service.features.log.service.ComInfLogAuditService;
import com.naas.admin_service.features.setting.model.ComCfgParameter;
import com.naas.admin_service.features.setting.repository.ComCfgParameterRepository;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobTriggerConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ComInfLogActivityService activityService;
    private final ComInfLogAuditService auditService;
    private final ComCfgParameterRepository comCfgParameterRepository;

    private final ObjectMapper objectMapper; // ✅ inject bean

    @Value("${job.kafka.topic.status:JOB-STATUS}")
    private String jobStatusTopic;

    @KafkaListener(
            topics = "${job.kafka.topic.id:JOB-ID}",
            groupId = "${job.kafka.consumer-group.clear-activity:CLEAR_ACTIVITY_WORKER}"
    )
    @Transactional
    public void consume(String messageJson) {
        try {
            // ✅ TenantContext đã được lib-core set sẵn từ Kafka header (multi=true)
            JobTriggerMessage msg = objectMapper.readValue(messageJson, JobTriggerMessage.class);

            if (!"CLEAR_LOG_ACTIVITY".equals(msg.getJobName())) return;

            log.info("Start CLEAR_LOG_ACTIVITY, logId={}", msg.getLogId());

            JobStatusMessage result = processClearLogActivity(msg);
            String json = objectMapper.writeValueAsString(result);

            // ✅ lib-core ProducerInterceptor tự add tenant header từ TenantContext (đang có trong thread listener)
            kafkaTemplate.send(jobStatusTopic, msg.getJobName(), json);

            log.info("Sent job status {} for logId={}", result.getStatus(), msg.getLogId());

        } catch (Exception e) {
            log.error("Failed to consume job trigger JSON: {}", messageJson, e);
        }
    }

    private JobStatusMessage processClearLogActivity(JobTriggerMessage msg) {
        String status = "SUCCESS";
        String resultMessage;

        try {
            int days = getCleaningDays();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime cutoffLdt = now.minusDays(days);
            Date cutoff = Date.from(cutoffLdt.atZone(ZoneId.systemDefault()).toInstant());

            List<String> deletedRequestIds = activityService.deleteActivityLogsBefore(cutoff);
            int deletedAudit = auditService.deleteActivityLogsByRequestIds(deletedRequestIds);

            resultMessage = String.format(
                    "Cleared %d, %d activity and audit logs older than %d day(s) (before %s)",
                    deletedRequestIds.size(), deletedAudit, days, cutoff
            );

        } catch (Exception e) {
            status = "FAILED";
            resultMessage = "Error: " + e.getMessage();
            log.error("Error while clearing activity logs", e);
        }

        JobStatusMessage result = new JobStatusMessage();
        result.setLogId(msg.getLogId());
        result.setJobId(msg.getJobId());
        result.setJobName(msg.getJobName());
        result.setStatus(status);
        result.setFinishedAt(new Date());
        result.setResultMessage(resultMessage);
        return result;
    }

    private int getCleaningDays() {
        ComCfgParameter parameter = comCfgParameterRepository
                .findByParamCodeAndIsActiveTrueAndIsDeleteFalse("LOG_CLEANING_TIME")
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "LOG_CLEANING_TIME"));

        final int days;
        try {
            days = Integer.parseInt(parameter.getParamValue());
        } catch (NumberFormatException ex) {
            throw new BusinessException(CommonErrorCode.INVALID_DATA_ENTITY);
        }

        if (days <= 0) throw new BusinessException(CommonErrorCode.INVALID_DATA_ENTITY);
        return days;
    }
}
