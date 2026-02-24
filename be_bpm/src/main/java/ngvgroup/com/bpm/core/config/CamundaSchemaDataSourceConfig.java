package ngvgroup.com.bpm.core.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class CamundaSchemaDataSourceConfig {

    @Bean(name = "camundaBpmDataSource")
    @ConfigurationProperties(prefix = "camunda.datasource")
    public HikariDataSource camundaBpmDataSource() {
        return new HikariDataSource();
    }

    @Bean(name = "camundaBpmTransactionManager")
    public DataSourceTransactionManager camundaBpmTransactionManager(
            @Qualifier("camundaBpmDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }
}
