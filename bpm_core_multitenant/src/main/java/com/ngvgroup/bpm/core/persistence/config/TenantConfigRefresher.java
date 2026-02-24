package com.ngvgroup.bpm.core.persistence.config;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public class TenantConfigRefresher implements AutoCloseable {

    private final MultitenancyProperties props;
    private final JdbcTenantDbConfigRegistry registry;
    private final DataSourceCache cache;

    private ScheduledExecutorService executor;

    public TenantConfigRefresher(MultitenancyProperties props,
                                 JdbcTenantDbConfigRegistry registry,
                                 DataSourceCache cache) {
        this.props = Objects.requireNonNull(props, "props");
        this.registry = Objects.requireNonNull(registry, "registry");
        this.cache = Objects.requireNonNull(cache, "cache");
    }

    /** gọi khi app start (AutoConfig sẽ gọi) */
    public synchronized void start() {
        if (!props.getRefresh().isEnabled()) {
            log.info("[MT] TenantConfigRefresher disabled (multitenancy.refresh.enabled=false)");
            return;
        }
        if (executor != null) return;

        long interval = Math.max(10_000L, props.getRefresh().getIntervalMs());
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "tenant-config-refresher");
            t.setDaemon(true);
            return t;
        });

        executor.scheduleWithFixedDelay(() -> {
            try {
                registry.refresh();
            } catch (Exception e) {
                log.warn("[MT] registry.refresh() failed", e);
            }

            try {
                cache.cleanUp(); // ✅ quan trọng để eviction chạy đều + close pool
            } catch (Exception e) {
                log.warn("[MT] cache.cleanUp() failed", e);
            }
        }, interval, interval, TimeUnit.MILLISECONDS);

        log.info("[MT] TenantConfigRefresher started, intervalMs={}", interval);
    }

    @Override
    public synchronized void close() {
        if (executor != null) {
            executor.shutdownNow();
            executor = null;
            log.info("[MT] TenantConfigRefresher stopped");
        }
    }
}
