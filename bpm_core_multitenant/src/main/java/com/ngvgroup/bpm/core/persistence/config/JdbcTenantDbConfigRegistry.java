package com.ngvgroup.bpm.core.persistence.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.event.EventListener; // ✅ annotation

@Slf4j
public class JdbcTenantDbConfigRegistry {

    private final JdbcTemplate jdbcTemplate;

    // cache theo tenant
    private volatile Map<String, TenantDbConfig> cache = Map.of();

    public JdbcTenantDbConfigRegistry(DataSource masterDataSource) {
        this.jdbcTemplate = new JdbcTemplate(masterDataSource);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        refresh();
    }

    public TenantDbConfig get(String tenantId) {
        TenantDbConfig cfg = cache.get(normTenant(tenantId));
        if (cfg == null) {
            throw new IllegalArgumentException(
                    "No COM_CFG_TENANT for tenant=" + tenantId +
                            ". Check COM_CFG_TENANT.ACTIVE='Y'."
            );
        }
        return cfg;
    }

    public Optional<TenantDbConfig> find(String tenantId) {
        return Optional.ofNullable(cache.get(normTenant(tenantId)));
    }

    public Set<String> listTenantIds() {
        if (cache.isEmpty()) return Set.of();
        return cache.values().stream()
                .map(TenantDbConfig::tenantId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public synchronized void refresh() {
        Map<String, TenantDbConfig> map = new HashMap<>();
        // ✅ query không còn filter SERVICE_CODE
        jdbcTemplate.query("""
                SELECT TENANT_ID, DB_TYPE, JDBC_URL, USERNAME, PASSWORD, DRIVER_CLASS
                FROM COM_CFG_TENANT
                WHERE ACTIVE='Y'
                """,
                rs -> {
                    TenantDbConfig cfg = new TenantDbConfig(
                            rs.getString("TENANT_ID"),
                            rs.getString("DB_TYPE"),
                            rs.getString("JDBC_URL"),
                            rs.getString("USERNAME"),
                            rs.getString("PASSWORD"),
                            rs.getString("DRIVER_CLASS")
                    );

                    String key = normTenant(cfg.tenantId());
                    if (map.containsKey(key)) {
                        log.warn("Duplicate ACTIVE tenant config detected for tenantId={}. Please keep only 1 ACTIVE='Y' row.", key);
                    }
                    map.put(key, cfg);

                }
        );

        if (map.isEmpty()) {
            log.warn("COM_CFG_TENANT is empty (ACTIVE='Y').");
            this.cache = Map.of();
            return;
        }

        this.cache = Map.copyOf(map);
        log.info("Loaded {} COM_CFG_TENANT rows", this.cache.size());
    }

    private String normTenant(String tenantId) {
        return tenantId == null ? null : tenantId.trim();
    }

    private String normalizeIssuer(String issuer) {
        String s = issuer.trim();
        while (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s;
    }
}
