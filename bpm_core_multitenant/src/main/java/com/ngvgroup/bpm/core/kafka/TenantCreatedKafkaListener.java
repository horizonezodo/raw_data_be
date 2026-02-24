package com.ngvgroup.bpm.core.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.liquibase.TenantSchemaLiquibaseRunner;
import com.ngvgroup.bpm.core.liquibase.TenantSchemaNaming;
import com.ngvgroup.bpm.core.liquibase.provision.OracleTenantSchemaProvisioner;
import com.ngvgroup.bpm.core.persistence.config.ServiceCodeProvider;
import com.ngvgroup.bpm.core.persistence.dto.TenantCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class TenantCreatedKafkaListener {

    private final OracleTenantSchemaProvisioner provisioner;
    private final TenantSchemaLiquibaseRunner liquibaseRunner;
    private final ServiceCodeProvider serviceCodeProvider;
    private final String schemaPassword;
    private final ObjectMapper objectMapper;

    public TenantCreatedKafkaListener(
            OracleTenantSchemaProvisioner provisioner,
            TenantSchemaLiquibaseRunner liquibaseRunner,
            ServiceCodeProvider serviceCodeProvider,
            String schemaPassword, ObjectMapper objectMapper
    ) {
        this.provisioner = provisioner;
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
        if (schemaPassword == null || schemaPassword.isBlank()) {
            throw new IllegalStateException("Missing required config: multitenancy.schema.password");
        }

        TenantCreatedEvent event = objectMapper.readValue(payload, TenantCreatedEvent.class);

        if (event == null || event.tenantId() == null || event.tenantId().isBlank()) return;

        String tenantId = event.tenantId().trim();
        String serviceCode = serviceCodeProvider.serviceCode();
        String schema = TenantSchemaNaming.buildOracleSchema(tenantId, serviceCode);

        log.info("[MT][TENANT_CREATED] recv tenant={} serviceCode={} schema={}", tenantId, serviceCode, schema);

        provisioner.ensureSchemaExists(tenantId, schema, schemaPassword);
        liquibaseRunner.migrateTenantSchema(tenantId, schema, schemaPassword);

        log.info("[MT][TENANT_CREATED] done tenant={} serviceCode={}", tenantId, serviceCode);
    }
}
