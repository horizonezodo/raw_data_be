package com.ngvgroup.bpm.core.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.kafka.TenantCreatedKafkaListener;
import com.ngvgroup.bpm.core.liquibase.*;
import com.ngvgroup.bpm.core.liquibase.provision.PostgresTenantSchemaProvisioner;
import com.ngvgroup.bpm.core.liquibase.provision.OracleTenantSchemaProvisioner;
import com.ngvgroup.bpm.core.liquibase.provision.TenantSchemaProvisioner;
import com.ngvgroup.bpm.core.liquibase.provision.TenantSchemaProvisionerRouter;
import com.ngvgroup.bpm.core.persistence.config.DataSourceCache;
import com.ngvgroup.bpm.core.persistence.config.JdbcTenantDbConfigRegistry;
import com.ngvgroup.bpm.core.persistence.config.ServiceCodeProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(TenantLiquibaseProperties.class)
@ConditionalOnProperty(prefix = "multitenancy", name = "enabled", havingValue = "true")
@ConditionalOnBean({JdbcTenantDbConfigRegistry.class, DataSourceCache.class, ServiceCodeProvider.class})
@ConditionalOnClass(name = "liquibase.Liquibase")
public class BpmCoreTenantLiquibaseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OracleTenantSchemaProvisioner oracleTenantSchemaProvisioner(
            JdbcTenantDbConfigRegistry registry,
            @org.springframework.beans.factory.annotation.Qualifier("defaultDataSource") javax.sql.DataSource defaultDataSource
    ) {
        return new OracleTenantSchemaProvisioner(registry, defaultDataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public PostgresTenantSchemaProvisioner postgresTenantSchemaProvisioner(
            JdbcTenantDbConfigRegistry registry,
            DataSourceCache cache
    ) {
        return new PostgresTenantSchemaProvisioner(registry, cache);
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantSchemaProvisionerRouter tenantSchemaProvisionerRouter(
            java.util.List<TenantSchemaProvisioner> provisioners
    ) {
        return new TenantSchemaProvisionerRouter(provisioners);
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantSchemaLiquibaseRunner tenantSchemaLiquibaseRunner(
            JdbcTenantDbConfigRegistry registry,
            DataSourceCache cache,
            TenantLiquibaseProperties props
    ) {
        return new TenantSchemaLiquibaseRunner(registry, cache, props);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "bpm.liquibase.tenant", name = "migrate-on-startup", havingValue = "true", matchIfMissing = true)
    public TenantLiquibaseStartupRunner tenantLiquibaseStartupRunner(
            JdbcTenantDbConfigRegistry registry,
            TenantSchemaLiquibaseRunner runner,
            TenantSchemaProvisionerRouter provisionerRouter,
            TenantLiquibaseProperties props,
            ServiceCodeProvider serviceCodeProvider,
            @Value("${multitenancy.schema.password}") String schemaPassword
    ) {
        return new TenantLiquibaseStartupRunner(registry, runner, provisionerRouter, props, serviceCodeProvider, schemaPassword);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "org.springframework.kafka.annotation.KafkaListener")
    public TenantCreatedKafkaListener tenantCreatedKafkaListener(
            JdbcTenantDbConfigRegistry registry,
            TenantSchemaProvisionerRouter provisionerRouter,
            TenantSchemaLiquibaseRunner runner,
            ServiceCodeProvider serviceCodeProvider,
            @Value("${multitenancy.schema.password}") String schemaPassword,
            ObjectMapper objectMapper
    ) {
        return new TenantCreatedKafkaListener(registry, provisionerRouter, runner, serviceCodeProvider, schemaPassword , objectMapper);
    }
}
