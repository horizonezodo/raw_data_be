package com.naas.admin_service.features.job.service.impl;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * ✅ Registry lưu các ScheduledFuture trong memory.
 * Tách riêng thành bean để tránh vòng phụ thuộc ScheduleJob <-> CancelJob
 *
 * key = tenantKey:jobId
 */
@Component
public class ScheduledTaskRegistry {

    private final Map<String, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    @SuppressWarnings("java:S1452")
    public Map<String, ScheduledFuture<?>> tasks() {
        return tasks;
    }
}
