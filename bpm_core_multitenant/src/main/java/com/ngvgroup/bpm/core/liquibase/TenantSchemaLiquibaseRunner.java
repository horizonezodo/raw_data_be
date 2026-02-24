package com.ngvgroup.bpm.core.liquibase;

import com.ngvgroup.bpm.core.persistence.config.DataSourceCache;
import com.ngvgroup.bpm.core.persistence.config.JdbcTenantDbConfigRegistry;
import com.ngvgroup.bpm.core.persistence.config.TenantDbConfig;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

@Slf4j
public record TenantSchemaLiquibaseRunner(
        JdbcTenantDbConfigRegistry registry,
        DataSourceCache cache,
        TenantLiquibaseProperties props
) {

    /**
     * ✅ Liquibase must connect as the TENANT schema user (OWNER = tenant schema).
     * Using MASTER + ALTER SESSION SET CURRENT_SCHEMA will NOT change object owner in Oracle.
     */
    public void migrateTenantSchema(String tenantId, String schema, String schemaPassword) throws Exception {
        TenantDbConfig base = registry.get(tenantId);

        String dbType = (base.dbType() == null) ? "UNKNOWN" : base.dbType().toUpperCase(Locale.ROOT);
        String schemaUpper = schema.toUpperCase(Locale.ROOT);

        // ✅ Build tenant credential for migration
        TenantDbConfig liquibaseCfg = base.withCreds(schemaUpper, schemaPassword);

        // ✅ Dedicated pool key for liquibase (avoid reuse with runtime/master)
        DataSource ds = cache.getOrCreateTenantLiquibase(tenantId, schemaUpper, liquibaseCfg);

        String changeLog = normalizeChangeLog(props.getChangeLog());

        log.info("[LIQUIBASE][TENANT_SCHEMA] start tenant={} schema={} dbType={} changelog={}",
                tenantId, schemaUpper, dbType, changeLog);

        try (Connection conn = ds.getConnection()) {
            if (props.isDebugConnection()) {
                logConn(conn, tenantId, schemaUpper);
            }

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(conn));

            database.setDefaultSchemaName(schemaUpper);
            database.setLiquibaseSchemaName(schemaUpper);

            var accessor = new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader());
            Liquibase liquibase = new Liquibase(changeLog, accessor, database);

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT SYS_CONTEXT('USERENV','CON_NAME') FROM dual")) {
                rs.next();
                log.info("[LIQUIBASE][DEBUG] CON_NAME={}", rs.getString(1));
            }

            // ✅ EXECUTE thật (không truyền Writer)
            liquibase.update(new Contexts(), new LabelExpression());
        }

        log.info("[LIQUIBASE][TENANT_SCHEMA] done tenant={} schema={}", tenantId, schemaUpper);
    }

    private void logConn(Connection conn, String tenantId, String schema) {
        try {
            DatabaseMetaData md = conn.getMetaData();
            log.info("[LIQUIBASE][DEBUG] phase={} tenant={} schema={} url={} user={}",
                    "BEFORE", tenantId, schema, md.getURL(), md.getUserName());
        } catch (Exception e) {
            log.warn("[LIQUIBASE][DEBUG] cannot read connection info phase={} tenant={} schema={}",
                    "BEFORE", tenantId, schema, e);
        }
    }

    private static String normalizeChangeLog(String s) {
        if (s == null) return null;
        String x = s.trim();
        if (x.startsWith("classpath:")) x = x.substring("classpath:".length());
        while (x.startsWith("/")) x = x.substring(1);
        return x;
    }
}
