package com.naas.admin_service.features.job.bootstrap;

import com.naas.admin_service.features.job.repository.ComCfgJobRepository;
import com.naas.admin_service.features.job.service.impl.ScheduleJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

/**
 * ✅ multitenancy.enabled=false (hoặc không khai báo):
 * - đọc COM_CFG_JOB trên spring.datasource và schedule như trước
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "multitenancy", name = "enabled", havingValue = "false", matchIfMissing = true)
public class SingleTenantJobBootstrap {

    private final ComCfgJobRepository cfgJobRepo;
    private final ScheduleJob scheduleJob;

    @PostConstruct
    @Transactional
    public void init() {
        var activeJobs = cfgJobRepo.findByIsActive(1);
        if (activeJobs.isEmpty()) {
            log.info("[SingleTenant] No active jobs to schedule on startup.");
            return;
        }

        activeJobs.forEach(job -> {
            try {
                scheduleJob.scheduleJobInternal(job, null); // null => DEFAULT tenant
            } catch (Exception e) {
                log.error("[SingleTenant] Failed to schedule job {} on startup", job.getJobName(), e);
            }
        });
    }
}
