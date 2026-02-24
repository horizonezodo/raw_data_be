package com.ngvgroup.bpm.core.liquibase;

import com.ngvgroup.bpm.core.liquibase.provision.OracleTenantSchemaProvisioner;
import com.ngvgroup.bpm.core.persistence.config.JdbcTenantDbConfigRegistry;
import com.ngvgroup.bpm.core.persistence.config.ServiceCodeProvider;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
public record TenantLiquibaseStartupRunner(JdbcTenantDbConfigRegistry registry,
                                           TenantSchemaLiquibaseRunner schemaLiquibaseRunner,
                                           OracleTenantSchemaProvisioner provisioner, TenantLiquibaseProperties props,
                                           ServiceCodeProvider serviceCodeProvider,
                                           String schemaPassword) implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {

        if (schemaPassword == null || schemaPassword.isBlank()) {
            throw new IllegalStateException("Missing required config: multitenancy.schema.password");
        }

        if (!props.isEnabled() || !props.isMigrateOnStartup()) {
            log.info("[LIQUIBASE][TENANT_SCHEMA] skipped (enabled={}, migrateOnStartup={})",
                    props.isEnabled(), props.isMigrateOnStartup());
            return;
        }

        Set<String> tenants = new LinkedHashSet<>(registry.listTenantIds());
        if (tenants.isEmpty()) {
            log.warn("[LIQUIBASE][TENANT_SCHEMA] no tenant found in TENANT_DB_CONFIG.");
            return;
        }

        String serviceCode = serviceCodeProvider.serviceCode();
        log.info("[LIQUIBASE][TENANT_SCHEMA] migrating {} tenants for serviceCode={}", tenants.size(), serviceCode);

        for (String tenantId : tenants) {
            try {
                String schema = TenantSchemaNaming.buildOracleSchema(tenantId, serviceCode);
                provisioner.ensureSchemaExists(tenantId, schema, schemaPassword);
                schemaLiquibaseRunner.migrateTenantSchema(tenantId, schema, schemaPassword);
            } catch (Exception ex) {
                log.error("[LIQUIBASE][TENANT_SCHEMA] failed tenant={}", tenantId, ex);
                throw new RuntimeException("Liquibase tenant schema migrate failed for " + tenantId, ex);
            }
        }

        log.info("[LIQUIBASE][TENANT_SCHEMA] all done.");
    }
}
