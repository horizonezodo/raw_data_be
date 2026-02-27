package com.ngvgroup.bpm.core.liquibase;

import com.ngvgroup.bpm.core.liquibase.provision.TenantSchemaProvisionerRouter;
import com.ngvgroup.bpm.core.persistence.config.JdbcTenantDbConfigRegistry;
import com.ngvgroup.bpm.core.persistence.config.ServiceCodeProvider;
import com.ngvgroup.bpm.core.persistence.config.TenantDbConfig;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
public record TenantLiquibaseStartupRunner(JdbcTenantDbConfigRegistry registry,
                                           TenantSchemaLiquibaseRunner schemaLiquibaseRunner,
                                           TenantSchemaProvisionerRouter provisionerRouter, TenantLiquibaseProperties props,
                                           ServiceCodeProvider serviceCodeProvider,
                                           String schemaPassword) implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        if (!props.isEnabled() || !props.isMigrateOnStartup()) {
            log.info("[LIQUIBASE][TENANT_SCHEMA] skipped (enabled={}, migrateOnStartup={})",
                    props.isEnabled(), props.isMigrateOnStartup());
            return;
        }

        registry.refresh();
        Set<String> tenants = new LinkedHashSet<>(registry.listTenantIds());
        if (tenants.isEmpty()) {
            log.warn("[LIQUIBASE][TENANT_SCHEMA] no tenant found in COM_CFG_TENANT.");
            return;
        }

        String serviceCode = serviceCodeProvider.serviceCode();
        log.info("[LIQUIBASE][TENANT_SCHEMA] migrating {} tenants for serviceCode={}", tenants.size(), serviceCode);

        for (String tenantId : tenants) {
            try {
                TenantDbConfig cfg = registry.get(tenantId);

                // schemaPassword is only required for Oracle (because we create USER schema owner)
                if ("ORACLE".equalsIgnoreCase(cfg.dbType())) {
                    if (schemaPassword == null || schemaPassword.isBlank()) {
                        throw new IllegalStateException("Missing required config: multitenancy.schema.password");
                    }
                }

                String schema = buildSchema(cfg.dbType(), tenantId, serviceCode);
                provisionerRouter.resolve(cfg.dbType()).ensureSchemaExists(tenantId, schema, schemaPassword);
                schemaLiquibaseRunner.migrateTenantSchema(tenantId, schema, schemaPassword);
            } catch (Exception ex) {
                log.error("[LIQUIBASE][TENANT_SCHEMA] failed tenant={}", tenantId, ex);
                throw new RuntimeException("Liquibase tenant schema migrate failed for " + tenantId, ex);
            }
        }

        log.info("[LIQUIBASE][TENANT_SCHEMA] all done.");
    }

    private static String buildSchema(String dbType, String tenantId, String serviceCode) {
        String t = (dbType == null) ? "" : dbType.trim().toUpperCase(java.util.Locale.ROOT);
        if ("POSTGRES".equals(t) || "POSTGRESQL".equals(t)) {
            return TenantSchemaNaming.buildPostgresSchema(tenantId, serviceCode);
        }
        // default to Oracle naming
        return TenantSchemaNaming.buildOracleSchema(tenantId, serviceCode);
    }
}
