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
        String schemaNorm = normalizeSchemaForDb(base.dbType(), schema);

        // ✅ Build migration credential:
        // - ORACLE: connect as schema owner (username == schema)
        // - POSTGRES: connect using tenant-row username/password (no role creation), but set default schema
        TenantDbConfig liquibaseCfg;
        if ("POSTGRES".equalsIgnoreCase(dbType) || "POSTGRESQL".equalsIgnoreCase(dbType)) {
            liquibaseCfg = base;
        } else {
            liquibaseCfg = base.withCreds(schemaNorm, schemaPassword);
        }

        // ✅ Dedicated pool key for liquibase (avoid reuse with runtime/master)
        DataSource ds = cache.getOrCreateTenantLiquibase(tenantId, schemaNorm, liquibaseCfg);

        String changeLog = normalizeChangeLog(props.getChangeLog());

        log.info("[LIQUIBASE][TENANT_SCHEMA] start tenant={} schema={} dbType={} changelog={}",
                tenantId, schemaNorm, dbType, changeLog);

        try (Connection conn = ds.getConnection()) {
            if (props.isDebugConnection()) {
                logConn(conn, tenantId, schemaNorm);
            }

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(conn));

            database.setDefaultSchemaName(schemaNorm);
            database.setLiquibaseSchemaName(schemaNorm);

            var accessor = new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader());
            Liquibase liquibase = new Liquibase(changeLog, accessor, database);


            if (props.getSchemaTargets() != null && !props.getSchemaTargets().isEmpty()) {
                props.getSchemaTargets().forEach((paramName, suffix) -> {
                    if (paramName == null || paramName.isBlank() || suffix == null || suffix.isBlank()) return;
                    liquibase.setChangeLogParameter(paramName, TenantSchemaNaming.buildOracleSchema(tenantId, suffix));
                });
            }

            // Oracle-only debug
            if ("ORACLE".equalsIgnoreCase(dbType)) {

                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery("SELECT SYS_CONTEXT('USERENV','CON_NAME') FROM dual")) {
                    rs.next();
                    log.info("[LIQUIBASE][DEBUG] CON_NAME={}", rs.getString(1));
                }
            }

            // ✅ EXECUTE thật (không truyền Writer)
            liquibase.update(new Contexts(), new LabelExpression());
        }

        log.info("[LIQUIBASE][TENANT_SCHEMA] done tenant={} schema={}", tenantId, schemaNorm);
    }


    private static String normalizeSchemaForDb(String dbType, String schema) {
        if (schema == null) return null;
        String s = schema.trim();
        if (dbType == null) return s;
        String t = dbType.trim().toUpperCase(Locale.ROOT);
        if ("ORACLE".equals(t)) return s.toUpperCase(Locale.ROOT);
        if ("POSTGRES".equals(t) || "POSTGRESQL".equals(t)) return s.toLowerCase(Locale.ROOT);
        return s;
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
