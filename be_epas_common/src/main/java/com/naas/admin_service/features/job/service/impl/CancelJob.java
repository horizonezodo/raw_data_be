package com.naas.admin_service.features.job.service.impl;

import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

/**
 * ✅ Cancel schedule theo tenant
 * - Không phụ thuộc ScheduleJob nữa -> hết circular dependency
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CancelJob {

    private static final String DEFAULT_TENANT_KEY = "_DEFAULT_";

    private final ScheduledTaskRegistry taskRegistry;

    public void cancelJobSchedule(Long jobId) {
        String tenantId = TenantContext.getTenantId();
        String safeTenant = (tenantId == null || tenantId.isBlank()) ? DEFAULT_TENANT_KEY : tenantId.trim();
        cancelJobSchedule(safeTenant, jobId);
    }

    public void cancelJobSchedule(String tenantId, Long jobId) {
        String safeTenant = (tenantId == null || tenantId.isBlank()) ? DEFAULT_TENANT_KEY : tenantId.trim();
        String key = safeTenant + ":" + jobId;

        ScheduledFuture<?> future = taskRegistry.tasks().remove(key);
        if (future != null) {
            future.cancel(false);
            log.info("Canceled scheduled job tenant={} jobId={}", safeTenant, jobId);
        }
    }
}
