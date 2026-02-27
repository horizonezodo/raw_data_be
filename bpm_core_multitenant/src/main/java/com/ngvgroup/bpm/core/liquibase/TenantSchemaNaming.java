package com.ngvgroup.bpm.core.liquibase;

import java.util.Locale;

public final class TenantSchemaNaming {
    private TenantSchemaNaming() {}

    public static String buildOracleSchema(String tenantId, String suffix) {
        String schema = (tenantId + "_" + suffix).toUpperCase(Locale.ROOT);
        schema = schema.replaceAll("[^A-Z0-9_]", "_");

        if (schema.length() > 30) {
            String hash = Integer.toHexString(schema.hashCode()).toUpperCase(Locale.ROOT);
            hash = (hash.length() > 6) ? hash.substring(0, 6) : String.format("%6s", hash).replace(' ', '0');
            schema = schema.substring(0, 23) + "_" + hash; // 30
        }
        return schema;
    }

    /**
     * Postgres identifier rules are less strict than Oracle (up to 63 bytes), and unquoted identifiers
     * are folded to lower-case. We generate lower-case to avoid quoting.
     */
    public static String buildPostgresSchema(String tenantId, String suffix) {
        String schema = (tenantId + "_" + suffix).toLowerCase(Locale.ROOT);
        schema = schema.replaceAll("[^a-z0-9_]", "_");

        if (!schema.isEmpty() && Character.isDigit(schema.charAt(0))) {
            schema = "t_" + schema;
        }

        if (schema.length() > 63) {
            String hash = Integer.toHexString(schema.hashCode()).toLowerCase(Locale.ROOT);
            hash = (hash.length() > 8) ? hash.substring(0, 8) : String.format("%8s", hash).replace(' ', '0');
            schema = schema.substring(0, 54) + "_" + hash; // 63
        }
        return schema;
    }

    /**
     * ✅ Helper chung để build schema theo DB_TYPE.
     * - ORACLE: schema = USER (uppercase, max 30)
     * - POSTGRES/POSTGRESQL: schema (lowercase, max 63)
     */
    public static String buildSchema(String dbType, String tenantId, String suffix) {
        String t = (dbType == null) ? "" : dbType.trim().toUpperCase(Locale.ROOT);

        if ("ORACLE".equals(t)) {
            return buildOracleSchema(tenantId, suffix);
        }
        if ("POSTGRES".equals(t) || "POSTGRESQL".equals(t)) {
            return buildPostgresSchema(tenantId, suffix);
        }
        // fallback an toàn
        return buildPostgresSchema(tenantId, suffix);
    }
}