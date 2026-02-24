package com.ngvgroup.bpm.core.liquibase.provision;

import com.ngvgroup.bpm.core.persistence.config.JdbcTenantDbConfigRegistry;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Locale;
import java.util.regex.Pattern;

@Slf4j
public record OracleTenantSchemaProvisioner(
        JdbcTenantDbConfigRegistry registry,
        DataSource masterDataSource
) {

    private static final Pattern ORACLE_IDENTIFIER = Pattern.compile("^[A-Z][A-Z0-9_]{0,29}$");

    // ✅ Default tablespace bạn đang gặp lỗi ORA-01950
    private static final String DEFAULT_TABLESPACE = "USERS";
    private static final String TEMP_TABLESPACE = "TEMP";

    public void ensureSchemaExists(String tenantId, String schema, String schemaPassword) throws Exception {

        if (masterDataSource == null) {
            throw new IllegalStateException("masterDataSource is required for provisioning tenant schema");
        }

        String user = schema.toUpperCase(Locale.ROOT);
        validateOracleIdentifier(user);

        getWithRefreshRetry(tenantId);

        try (Connection conn = masterDataSource.getConnection()) {
            if (existsUser(conn, user)) {
                // ✅ đảm bảo user đã có quota (tránh case user tạo trước nhưng thiếu quota)
                ensureQuotaAndBasics(conn, user);
                log.info("[MT][SCHEMA] exists tenant={} schema={}", tenantId, user);
                return;
            }

            log.info("[MT][SCHEMA] creating tenant={} schema={}", tenantId, user);
            try (Statement st = conn.createStatement()) {
                String pw = escapeOracleQuoted(schemaPassword);

                // ✅ Create user + set tablespaces
                st.execute("CREATE USER " + user
                        + " IDENTIFIED BY \"" + pw + "\""
                        + " DEFAULT TABLESPACE " + DEFAULT_TABLESPACE
                        + " TEMPORARY TABLESPACE " + TEMP_TABLESPACE);

                // ✅ QUOTA: cái này là nguyên nhân ORA-01950
                st.execute("ALTER USER " + user + " QUOTA UNLIMITED ON " + DEFAULT_TABLESPACE);

                // ✅ Basic login
                st.execute("GRANT CREATE SESSION TO " + user);

                // ✅ “Full quyền tạo object trong schema của chính nó” (Liquibase hay cần)
                st.execute("GRANT CREATE TABLE TO " + user);
                st.execute("GRANT CREATE SEQUENCE TO " + user);
                st.execute("GRANT CREATE VIEW TO " + user);
                st.execute("GRANT CREATE PROCEDURE TO " + user);
                st.execute("GRANT CREATE TRIGGER TO " + user);
                st.execute("GRANT CREATE TYPE TO " + user);
                st.execute("GRANT CREATE SYNONYM TO " + user);

                // (Optional) Nếu changelog có materialized view
                // st.execute("GRANT CREATE MATERIALIZED VIEW TO " + user);

                // (Optional) Nếu bạn muốn “khỏi nghĩ tablespace”, rất rộng quyền:
                // st.execute("GRANT UNLIMITED TABLESPACE TO " + user);

                // ✅ (Optional) nếu bạn có dùng job/scheduler trong changelog
                // st.execute("GRANT CREATE JOB TO " + user);
            }

            log.info("[MT][SCHEMA] created tenant={} schema={} tablespace={}", tenantId, user, DEFAULT_TABLESPACE);
        }
    }

    /** đảm bảo user đã có quota + tablespace đúng, dùng cho case user tồn tại nhưng thiếu quota */
    private void ensureQuotaAndBasics(Connection conn, String user) throws SQLException {
        try (Statement st = conn.createStatement()) {
            // set default/temp tablespace (không lỗi nếu đã đúng)
            try { st.execute("ALTER USER " + user + " DEFAULT TABLESPACE " + DEFAULT_TABLESPACE); } catch (SQLException ignore) {}
            try { st.execute("ALTER USER " + user + " TEMPORARY TABLESPACE " + TEMP_TABLESPACE); } catch (SQLException ignore) {}

            // quota (fix ORA-01950)
            try { st.execute("ALTER USER " + user + " QUOTA UNLIMITED ON " + DEFAULT_TABLESPACE); } catch (SQLException ignore) {}

            // create session (nếu thiếu)
            try { st.execute("GRANT CREATE SESSION TO " + user); } catch (SQLException ignore) {}
        }
    }

    private void validateOracleIdentifier(String identifier) {
        if (identifier == null || !ORACLE_IDENTIFIER.matcher(identifier).matches()) {
            throw new IllegalArgumentException("Invalid Oracle schema identifier: " + identifier);
        }
    }

    private String escapeOracleQuoted(String s) {
        if (s == null) return "";
        return s.replace("\"", "\"\"");
    }

    private void getWithRefreshRetry(String tenantId) throws InterruptedException {
        int max = 5;
        for (int i = 1; i <= max; i++) {
            if (registry.find(tenantId).isPresent()) return;

            registry.refresh();
            Thread.sleep(300L * i);
        }
        log.warn("[MT][SCHEMA] TENANT_DB_CONFIG not found after retries for tenant={}. Continue provisioning anyway.", tenantId);
    }

    private boolean existsUser(Connection conn, String username) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(1) FROM ALL_USERS WHERE USERNAME = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}
