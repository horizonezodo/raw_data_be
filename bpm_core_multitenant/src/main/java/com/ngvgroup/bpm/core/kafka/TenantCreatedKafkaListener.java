package com.ngvgroup.bpm.core.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.liquibase.TenantSchemaLiquibaseRunner;
import com.ngvgroup.bpm.core.liquibase.TenantSchemaNaming;
import com.ngvgroup.bpm.core.liquibase.provision.TenantSchemaProvisionerRouter;
import com.ngvgroup.bpm.core.persistence.config.JdbcTenantDbConfigRegistry;
import com.ngvgroup.bpm.core.persistence.config.ServiceCodeProvider;
import com.ngvgroup.bpm.core.persistence.dto.TenantCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class TenantCreatedKafkaListener {

    private final JdbcTenantDbConfigRegistry registry;
    private final TenantSchemaProvisionerRouter provisionerRouter;
    private final TenantSchemaLiquibaseRunner liquibaseRunner;
    private final ServiceCodeProvider serviceCodeProvider;
    private final String schemaPassword;
    private final ObjectMapper objectMapper;

    public TenantCreatedKafkaListener(
            JdbcTenantDbConfigRegistry registry,
            TenantSchemaProvisionerRouter provisionerRouter,
            TenantSchemaLiquibaseRunner liquibaseRunner,
            ServiceCodeProvider serviceCodeProvider,
            String schemaPassword, ObjectMapper objectMapper
    ) {
        this.registry = registry;
        this.provisionerRouter = provisionerRouter;
        this.liquibaseRunner = liquibaseRunner;
        this.serviceCodeProvider = serviceCodeProvider;
        this.schemaPassword = schemaPassword;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${multitenancy.kafka.topic}",
            groupId = "${multitenancy.kafka.group}"
    )
    public void onTenantCreated(String payload) throws Exception {
        TenantCreatedEvent event = objectMapper.readValue(payload, TenantCreatedEvent.class);

        if (event == null || event.tenantId() == null || event.tenantId().isBlank()) return;

        String tenantId = event.tenantId().trim();
        String serviceCode = serviceCodeProvider.serviceCode();
        registry.refresh();
        var cfg = registry.get(tenantId);

        // schemaPassword is only required for Oracle (because we create USER schema owner)
        if ("ORACLE".equalsIgnoreCase(cfg.dbType())) {
            if (schemaPassword == null || schemaPassword.isBlank()) {
                throw new IllegalStateException("Missing required config: multitenancy.schema.password");
            }
        }

        String schema = buildSchema(cfg.dbType(), tenantId, serviceCode);

        log.info("[MT][TENANT_CREATED] recv tenant={} serviceCode={} schema={}", tenantId, serviceCode, schema);

        provisionerRouter.resolve(cfg.dbType()).ensureSchemaExists(tenantId, schema, schemaPassword);
        liquibaseRunner.migrateTenantSchema(tenantId, schema, schemaPassword);

        log.info("[MT][TENANT_CREATED] done tenant={} serviceCode={}", tenantId, serviceCode);
    }

    private static String buildSchema(String dbType, String tenantId, String serviceCode) {
        String t = (dbType == null) ? "" : dbType.trim().toUpperCase(java.util.Locale.ROOT);
        if ("POSTGRES".equals(t) || "POSTGRESQL".equals(t)) {
            return TenantSchemaNaming.buildPostgresSchema(tenantId, serviceCode);
        }
        return TenantSchemaNaming.buildOracleSchema(tenantId, serviceCode);
    }
}
