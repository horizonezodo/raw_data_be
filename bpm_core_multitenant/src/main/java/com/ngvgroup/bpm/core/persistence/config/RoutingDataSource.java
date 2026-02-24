package com.ngvgroup.bpm.core.persistence.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

@Slf4j
public class RoutingDataSource extends AbstractDataSource {

    private final JdbcTenantDbConfigRegistry registry;
    private final DataSourceCache cache;
    private final DataSource defaultDataSource;

    public RoutingDataSource(
            JdbcTenantDbConfigRegistry registry,
            DataSourceCache cache,
            DataSource defaultDataSource
    ) {
        this.registry = registry;
        this.cache = cache;
        this.defaultDataSource = defaultDataSource;
    }

    private DataSource resolve() {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank() || "master".equalsIgnoreCase(tenantId)) {
            return defaultDataSource;
        }

        TenantDbConfig cfg = registry.find(tenantId).orElse(null);
        if (cfg == null) {
            log.warn("[MT] tenant={} has no TENANT_DB_CONFIG fallback to spring.datasource",
                    tenantId);
            return defaultDataSource;
        }

        String dbType = (cfg.dbType() == null) ? "UNKNOWN" : cfg.dbType().toUpperCase(Locale.ROOT);
        String key = tenantId + ":" + dbType;
        return cache.getOrCreate(key, cfg);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return resolve().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return resolve().getConnection(username, password);
    }
}
