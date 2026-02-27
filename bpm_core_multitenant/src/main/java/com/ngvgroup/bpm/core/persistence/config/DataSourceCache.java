package com.ngvgroup.bpm.core.persistence.config;

import com.github.benmanes.caffeine.cache.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Locale;
import java.util.Objects;

/**
 * Cache datasource theo key (tenant + service + dbType + purpose).
 *
 * ✅ MASTER datasource:
 * - dùng luôn spring.datasource (defaultDataSource) để provisioning schema/user (CREATE USER/GRANT)
 *
 * ✅ TENANT datasource:
 * - build theo TenantDbConfig (username/password tenant)
 * - key phải đủ entropy để tránh reuse nhầm (RUNTIME vs LIQUIBASE)
 */
@Slf4j
public class DataSourceCache {

    private final MultitenancyProperties props;
    private final ServiceCodeProvider serviceCodeProvider;
    private final DataSource masterDataSource;
    private final Cache<String, HikariDataSource> cache;

    public DataSourceCache(MultitenancyProperties props,
                           ServiceCodeProvider serviceCodeProvider,
                           DataSource masterDataSource) {
        this.props = Objects.requireNonNull(props, "props");
        this.serviceCodeProvider = Objects.requireNonNull(serviceCodeProvider, "serviceCodeProvider");
        this.masterDataSource = Objects.requireNonNull(masterDataSource, "masterDataSource");

        long max = props.getPool().getCacheMaxSize();

        this.cache = Caffeine.newBuilder()
                .maximumSize(max <= 0 ? 500 : max)
                .expireAfterAccess(Duration.ofMillis(props.getPool().getIdleTimeoutMs()))
                .removalListener((String key, HikariDataSource ds, RemovalCause cause) -> {
                    if (ds != null) {
                        try {
                            ds.close();
                            log.info("[MT] Closed datasource key={}, cause={}", key, cause);
                        } catch (Exception e) {
                            log.warn("[MT] Error closing datasource key={}, cause={}", key, cause, e);
                        }
                    }
                })
                .build();
    }

    /** ✅ MASTER datasource (spring.datasource / defaultDataSource). */
    public DataSource master() {
        return masterDataSource;
    }

    /** Generic getOrCreate for tenant DS (kept for backward compatibility). */
    public HikariDataSource getOrCreate(String key, TenantDbConfig cfg) {
        return cache.get(key, k -> createDataSource(k, cfg));
    }

    /**
     * ✅ Datasource dùng cho PROVISIONING (tạo schema/user).
     *
     * - Provisioning có thể chạy trước khi schema tồn tại, nên tuyệt đối KHÔNG set
     *   connectionInitSql kiểu "ALTER SESSION SET CURRENT_SCHEMA=..." (Oracle) hay
     *   "SET search_path TO ..." (Postgres).
     * - Key phải đủ entropy (dbType + jdbcUrl) để tránh reuse nhầm datasource (khi tenantId giữ nguyên
     *   nhưng bạn đổi COM_CFG_TENANT từ ORACLE -> POSTGRES hoặc đổi jdbcUrl).
     */
    public HikariDataSource getOrCreateTenantProvision(String tenantId, TenantDbConfig cfg) {
        String dbType = (cfg.dbType() == null) ? "UNKNOWN" : cfg.dbType().trim().toUpperCase(Locale.ROOT);
        String jdbcHash = Integer.toHexString(Objects.toString(cfg.jdbcUrl(), "").hashCode());
        String key = "TENANT:" + tenantId + ":" + dbType + ":" + jdbcHash + ":PROVISION";
        return getOrCreate(key, cfg);
    }

    /** ✅ Recommended: tenant runtime pool */
    public HikariDataSource getOrCreateTenantRuntime(String tenantId, String schema, TenantDbConfig cfg) {
        String dbType = (cfg.dbType() == null) ? "UNKNOWN" : cfg.dbType().trim().toUpperCase(Locale.ROOT);
        String schemaUpper = (schema == null) ? "" : schema.trim().toUpperCase(Locale.ROOT);
        String key = "TENANT:" + cfg.tenantId() + ":" + schemaUpper + ":" + dbType + ":RUNTIME";
        return getOrCreate(key, cfg);
    }

    /** ✅ Recommended: tenant liquibase pool (connect as tenant user => owner đúng) */
    public HikariDataSource getOrCreateTenantLiquibase(String tenantId, String schema, TenantDbConfig cfg) {
        String dbType = (cfg.dbType() == null) ? "UNKNOWN" : cfg.dbType().trim().toUpperCase(Locale.ROOT);
        String schemaUpper = (schema == null) ? "" : schema.trim().toUpperCase(Locale.ROOT);
        String key = "TENANT:" + cfg.tenantId() + ":" + schemaUpper + ":" + dbType + ":LIQUIBASE";
        return getOrCreate(key, cfg);
    }

    public void invalidate(String key) {
        cache.invalidate(key);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }

    public void cleanUp() {
        cache.cleanUp();
    }

    private HikariDataSource createDataSource(String key, TenantDbConfig cfg) {
        HikariConfig hc = new HikariConfig();

        hc.setJdbcUrl(cfg.jdbcUrl());
        hc.setUsername(cfg.username());
        hc.setPassword(cfg.password());

        if (cfg.driverClass() != null && !cfg.driverClass().isBlank()) {
            hc.setDriverClassName(cfg.driverClass().trim());
        }

        hc.setMaximumPoolSize(Math.max(1, props.getPool().getMaxSize()));
        hc.setMinimumIdle(Math.max(0, props.getPool().getMinIdle()));
        hc.setIdleTimeout(props.getPool().getIdleTimeoutMs());
        hc.setMaxLifetime(props.getPool().getMaxLifetimeMs());
        hc.setConnectionTimeout(props.getPool().getConnectionTimeoutMs());

        // ✅ PROVISION pool: KHÔNG set schema/search_path vì schema có thể CHƯA tồn tại.
        boolean isProvision = key != null && key.contains(":PROVISION");

        String serviceCode = serviceCodeProvider.serviceCode();
        String schema = null;
        if (!isProvision) {
            // ✅ Build schema = {tenantId}_{serviceCode} theo pattern (runtime use-case)
            String rawSchema = props.resolveSchema(cfg.tenantId(), serviceCode);
            schema = sanitizeSchema(rawSchema);

            // ✅ Nếu username chính là schema owner thì không cần set initSql
            if (schema != null && cfg.username() != null && schema.equalsIgnoreCase(cfg.username().trim())) {
                schema = null;
            }

            String initSql = buildInitSql(cfg.dbType(), schema);
            if (initSql != null) {
                hc.setConnectionInitSql(initSql);
            }
        }

        log.info("[MT] Created datasource key={}, tenant={}, serviceCode={}, dbType={}, schema={}",
                key, cfg.tenantId(), serviceCode, cfg.dbType(),
                (isProvision ? "(provision/no-schema)" : (schema == null ? "(owner)" : schema)));

        return new HikariDataSource(hc);
    }

    private String sanitizeSchema(String schema) {
        if (schema == null || schema.isBlank()) return null;

        String s = schema.trim().replaceAll("[^a-zA-Z0-9_]", "_");
        if (!s.isEmpty() && Character.isDigit(s.charAt(0))) {
            s = "t_" + s;
        }
        return s;
    }

    private String buildInitSql(String dbType, String schema) {
        if (schema == null || schema.isBlank()) return null;

        String s = schema.trim();
        if (dbType == null) return null;

        String t = dbType.trim().toUpperCase(Locale.ROOT);

        if ("ORACLE".equals(t)) {
            return "ALTER SESSION SET CURRENT_SCHEMA=" + s;
        }

        if ("POSTGRES".equals(t) || "POSTGRESQL".equals(t)) {
            return "SET search_path TO " + s;
        }

        return null;
    }
}
