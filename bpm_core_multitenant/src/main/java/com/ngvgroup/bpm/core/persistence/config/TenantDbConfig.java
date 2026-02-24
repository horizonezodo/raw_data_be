package com.ngvgroup.bpm.core.persistence.config;

public record TenantDbConfig(
        String tenantId,
        String dbType,       // ORACLE / POSTGRES
        String jdbcUrl,
        String username,
        String password,
        String driverClass,
        String issuerUri
) {
    public TenantDbConfig withUsername(String u) {
        return new TenantDbConfig(tenantId, dbType, jdbcUrl, u, password, driverClass, issuerUri);
    }

    public TenantDbConfig withPassword(String p) {
        return new TenantDbConfig(tenantId, dbType, jdbcUrl, username, p, driverClass, issuerUri);
    }

    public TenantDbConfig withCreds(String u, String p) {
        return new TenantDbConfig(tenantId, dbType, jdbcUrl, u, p, driverClass, issuerUri);
    }
}
