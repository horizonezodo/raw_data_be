package ngvgroup.com.bpm.core.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = "multitenancy", name = "enabled", havingValue = "false", matchIfMissing = true)
public class SingleTenantDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties businessDsProps() {
        return new DataSourceProperties();
    }

    @Bean(name = "dataSource")
    @Primary
    public DataSource businessDataSource(DataSourceProperties businessDsProps) {
        return businessDsProps.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }
}