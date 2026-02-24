package com.naas.admin_service.features.job.service.impl;

import com.naas.admin_service.features.job.model.ComCfgJob;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.concurrent.ScheduledFuture;

/**
 * ✅ Tenant-aware schedule:
 * - key schedule = tenantKey:jobId
 * - Runnable tự set TenantContext trước khi run (scheduler thread không có request)
 * - Dùng ScheduledTaskRegistry để lưu task (tránh vòng phụ thuộc)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleJob {

    private static final String DEFAULT_TENANT_KEY = "_DEFAULT_";

    private final CancelJob cancelJob;
    private final ThreadPoolTaskScheduler jobTaskScheduler;
    private final RunSingle runSingle;
    private final ScheduledTaskRegistry taskRegistry;

    /**
     * Dùng trong request flow: tenantId lấy từ TenantContext.
     */
    @Transactional
    public void scheduleJobInternal(ComCfgJob job) {
        String tenantId = TenantContext.getTenantId();
        scheduleJobInternal(job, tenantId);
    }

    /**
     * Dùng cho bootstrap startup (quét nhiều tenant): tenantId truyền rõ ràng.
     */
    public void scheduleJobInternal(ComCfgJob job, String tenantId) {
        if (job.getIsActive() == null || job.getIsActive() != 1) {
            log.info("Job {} (id={}) is not active, skip scheduling", job.getJobName(), job.getId());
            return;
        }

        String safeTenant = (tenantId == null || tenantId.isBlank()) ? DEFAULT_TENANT_KEY : tenantId.trim();
        String key = safeTenant + ":" + job.getId();

        // ✅ huỷ schedule cũ nếu có (theo tenant)
        cancelJob.cancelJobSchedule(safeTenant, job.getId());

        Long jobId = job.getId();
        String jobName = job.getJobName();

        CronTrigger cronTrigger = new CronTrigger(job.getCronExpr(), ZoneId.systemDefault());

        Runnable task = () -> {
            if (!DEFAULT_TENANT_KEY.equals(safeTenant)) {
                TenantContext.setTenantId(safeTenant);
            }
            try {
                runSingle.runSingleExecution(jobId, jobName);
            } catch (Exception e) {
                log.error("Error while executing tenant={} jobId={}", safeTenant, jobId, e);
            } finally {
                TenantContext.clear();
            }
        };

        ScheduledFuture<?> future = jobTaskScheduler.schedule(task, cronTrigger);

        // ✅ lưu vào registry
        taskRegistry.tasks().put(key, future);

        log.info("Scheduled tenant={} job {} (id={}) cron={}", safeTenant, jobName, jobId, job.getCronExpr());
    }
}
