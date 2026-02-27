package com.ngvgroup.bpm.core.liquibase.provision;

import com.ngvgroup.bpm.core.persistence.config.DataSourceCache;
import com.ngvgroup.bpm.core.persistence.config.JdbcTenantDbConfigRegistry;
import com.ngvgroup.bpm.core.persistence.config.TenantDbConfig;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Postgres provisioning (per your requirement):
 * <ul>
 *   <li>NO ROLE/USER creation</li>
 *   <li>Use the {@code USERNAME/PASSWORD} in COM_CFG_TENANT row to create schemas + tables</li>
 *   <li>Ensure SCHEMA exists for each service</li>
 * </ul>
 */
@Slf4j
public record PostgresTenantSchemaProvisioner(JdbcTenantDbConfigRegistry registry,
                                              DataSourceCache cache) implements TenantSchemaProvisioner {

    private static final Pattern PG_IDENTIFIER = Pattern.compile("^[a-z_][a-z0-9_]{0,62}$");

    @Override
    public boolean supports(String dbType) {
        if (dbType == null) return false;
        String t = dbType.trim().toUpperCase(Locale.ROOT);
        return "POSTGRES".equals(t) || "POSTGRESQL".equals(t);
    }

    @Override
    public void ensureSchemaExists(String tenantId, String schema, String schemaPassword) throws Exception {
        TenantDbConfig base = registry.get(tenantId);

        String schemaName = normalizeIdentifier(schema);
        validate(schemaName);

        // ✅ Kết nối bằng chính USERNAME/PASSWORD của tenant Postgres trong COM_CFG_TENANT.
        // Lưu ý: KHÔNG được reuse nhầm datasource Oracle.
        // Vì vậy dùng key PROVISION có entropy (dbType + jdbcUrl) để tránh cache dính datasource cũ.
        DataSource ds = cache.getOrCreateTenantProvision(tenantId, base);

        try (Connection conn = ds.getConnection()) {
            if (!schemaExists(conn, schemaName)) {
                log.info("[MT][PG][SCHEMA] creating schema tenant={} schema={}", tenantId, schemaName);
                createSchema(conn, schemaName);
            }

            ensureBasicGrants(conn, schemaName);
        }

        log.info("[MT][PG][SCHEMA] ready tenant={} schema={}", tenantId, schemaName);
    }

    private static String normalizeIdentifier(String s) {
        if (s == null) return null;
        String x = s.trim().toLowerCase(Locale.ROOT);
        x = x.replaceAll("[^a-z0-9_]", "_");
        if (!x.isEmpty() && Character.isDigit(x.charAt(0))) x = "t_" + x;
        if (x.length() > 63) x = x.substring(0, 63);
        return x;
    }

    private static void validate(String id) {
        if (id == null || !PG_IDENTIFIER.matcher(id).matches()) {
            throw new IllegalArgumentException("Invalid Postgres identifier: " + id);
        }
    }

    private static boolean schemaExists(Connection conn, String schema) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM information_schema.schemata WHERE schema_name = ?")) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static void createSchema(Connection conn, String schema) throws Exception {
        try (Statement st = conn.createStatement()) {
            // owner will be current user
            st.execute("CREATE SCHEMA IF NOT EXISTS " + schema);
        }
    }

    private static void ensureBasicGrants(Connection conn, String schema) throws Exception {
        try (Statement st = conn.createStatement()) {
            try {
                st.execute("GRANT USAGE, CREATE ON SCHEMA " + schema + " TO CURRENT_USER");
            } catch (Exception ignore) {
            }
        }
    }
}
