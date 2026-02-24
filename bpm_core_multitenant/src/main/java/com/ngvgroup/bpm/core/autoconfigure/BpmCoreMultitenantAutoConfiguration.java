package com.ngvgroup.bpm.core.autoconfigure;

import com.ngvgroup.bpm.core.persistence.config.*;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@AutoConfiguration(before = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(MultitenancyProperties.class)
@ConditionalOnProperty(prefix = "multitenancy", name = "enabled", havingValue = "true")
public class BpmCoreMultitenantAutoConfiguration {

    @Bean(name = "defaultDataSource")
    @LiquibaseDataSource // đợi liqui master chạy trước
    public DataSource defaultDataSource(DataSourceProperties props) {
        return props.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public JdbcTenantDbConfigRegistry jdbcTenantDbConfigRegistry(
            @Qualifier("defaultDataSource") DataSource defaultDataSource
    ) {
        return new JdbcTenantDbConfigRegistry(defaultDataSource);
    }

    @Bean
    public DataSourceCache dataSourceCache(MultitenancyProperties props,
                                               ServiceCodeProvider serviceCodeProvider,
                                               @Qualifier("defaultDataSource") DataSource defaultDataSource) {
        return new DataSourceCache(props, serviceCodeProvider, defaultDataSource);
    }

    @Bean
    @Primary
    public DataSource routingDataSource(
            JdbcTenantDbConfigRegistry registry,
            DataSourceCache cache,
            @Qualifier("defaultDataSource") DataSource defaultDataSource
    ) {
        return new RoutingDataSource(registry, cache, defaultDataSource);
    }

    @Bean
    public TenantConfigRefresher tenantConfigRefresher(MultitenancyProperties props,
                                                       JdbcTenantDbConfigRegistry registry,
                                                       DataSourceCache cache) {
        TenantConfigRefresher refresher = new TenantConfigRefresher(props, registry, cache);
        refresher.start();
        return refresher;
    }
}
