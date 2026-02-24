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
}