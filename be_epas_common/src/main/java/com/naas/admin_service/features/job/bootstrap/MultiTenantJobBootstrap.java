package com.naas.admin_service.features.job.bootstrap;

import com.naas.admin_service.features.job.repository.ComCfgJobRepository;
import com.naas.admin_service.features.job.service.impl.ScheduleJob;
import com.ngvgroup.bpm.core.liquibase.TenantSchemaNaming;
import com.ngvgroup.bpm.core.persistence.config.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Set;

/**
 * ✅ multitenancy.enabled=true:
 * - serviceCode = spring.application.name
 * - quét tất cả TENANT_DB_CONFIG theo SERVICE_CODE
 * - tenant DB không có COM_CFG_JOB => log warn + skip
 * ✅ Dùng ObjectProvider để:
 * - IntelliJ không gạch đỏ "No beans found"
 * - An toàn nếu auto-config chưa load (registry/cache null) => skip thay vì
 * crash
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "multitenancy", name = "enabled", havingValue = "true")
public class MultiTenantJobBootstrap {

    private final ObjectProvider<JdbcTenantDbConfigRegistry> registryProvider;
    private final ObjectProvider<DataSourceCache> dsCacheProvider;
    private final ObjectProvider<ServiceCodeProvider> serviceCodeProviderProvider;

    private final ComCfgJobRepository cfgJobRepo;
    private final ScheduleJob scheduleJob;

    // @PostConstruct
    @EventListener(ApplicationReadyEvent.class)
    @Order(Ordered.LOWEST_PRECEDENCE) // ✅ chạy SAU liquibase/provision (tránh job boot trước khi schema/user được
                                      // tạo)
    public void bootstrap() {
        JdbcTenantDbConfigRegistry registry = registryProvider.getIfAvailable();
        DataSourceCache dsCache = dsCacheProvider.getIfAvailable();
        ServiceCodeProvider serviceCodeProvider = serviceCodeProviderProvider.getIfAvailable();

        // ✅ Nếu thiếu bean (do auto-config chưa register), không crash
        if (registry == null || dsCache == null || serviceCodeProvider == null) {
            log.warn(
                    "[MultiTenant] Missing multi-tenant beans (registry/cache/serviceCodeProvider). Skip bootstrap jobs.");
            return;
        }

        registry.refresh();
        Set<String> tenantIds = registry.listTenantIds();

        if (tenantIds.isEmpty()) {
            log.warn("[MultiTenant] No tenant config found for. Skip scheduling.");
            return;
        }

        log.info("[MultiTenant] Bootstrap jobs for {} tenants", tenantIds.size());

        for (String tenantId : tenantIds) {
            try {
                scheduleTenantJobs(tenantId, registry, dsCache);
            } catch (Exception e) {
                log.error("[MultiTenant] Failed bootstrap tenant={}", tenantId, e);
            }
        }
    }

    protected void scheduleTenantJobs(String tenantId,
            JdbcTenantDbConfigRegistry registry,
            DataSourceCache dsCache) {

        TenantDbConfig cfg = registry.get(tenantId);
        // ✅ IMPORTANT (Oracle): service schema = 1 USER riêng (vd: EPAS_MANH_COMMON)
        // Nếu job bootstrap tạo DS bằng user trong TENANT_DB_CONFIG (vd:
        // TENANT_NGV_DEMO)
        // rồi ALTER SESSION SET CURRENT_SCHEMA=EPAS_MANH_COMMON => sẽ ORA-01435 khi
        // user/schema chưa được provision.
        // Vì vậy: luôn resolve schema theo (tenantId + serviceCode) và lấy DS runtime
        // đúng schema.
        String serviceCode = serviceCodeProviderProvider.getIfAvailable().serviceCode();
        String schema = TenantSchemaNaming.buildSchema(cfg.dbType(), tenantId, serviceCode);
        DataSource tenantDs = dsCache.getOrCreateTenantRuntime(tenantId, schema, cfg);

        // ✅ tenant thiếu COM_CFG_JOB => skip + log
        if (!hasComCfgJobTable(tenantDs)) {
            log.warn("[MultiTenant] tenant={} does NOT have COM_CFG_JOB -> SKIP", tenantId);
            return;
        }

        TenantContext.setTenantId(tenantId);
        try {
            var activeJobs = cfgJobRepo.findByIsActive(1);
            if (activeJobs.isEmpty()) {
                log.info("[MultiTenant] tenant={} no active jobs", tenantId);
                return;
            }

            activeJobs.forEach(job -> {
                try {
                    scheduleJob.scheduleJobInternal(job, tenantId);
                } catch (Exception e) {
                    log.error("[MultiTenant] tenant={} failed schedule job={}", tenantId, job.getJobName(), e);
                }
            });

            log.info("[MultiTenant] tenant={} scheduled {} jobs", tenantId, activeJobs.size());
        } finally {
            TenantContext.clear();
        }
    }

    private boolean hasComCfgJobTable(DataSource ds) {
        try {
            JdbcTemplate jdbc = new JdbcTemplate(ds);
            jdbc.query("SELECT 1 FROM COM_CFG_JOB WHERE 1=0", rs -> {
            });
            return true;
        } catch (DataAccessException ex) {
            return false;
        }
    }
}
