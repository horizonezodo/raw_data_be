package ngvgroup.com.bpm.core.config;

import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.util.SpringBootProcessEnginePlugin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
public class CamundaForceDsTmPlugin extends SpringBootProcessEnginePlugin {

    private final DataSource camundaDs;
    private final PlatformTransactionManager camundaTm;

    public CamundaForceDsTmPlugin(
            @Qualifier("camundaBpmDataSource") DataSource camundaDs,
            @Qualifier("camundaBpmTransactionManager") PlatformTransactionManager camundaTm) {
        this.camundaDs = camundaDs;
        this.camundaTm = camundaTm;
    }

    @Override
    public void preInit(SpringProcessEngineConfiguration cfg) {
        cfg.setDataSource(camundaDs);
        cfg.setTransactionManager(camundaTm);
    }
}
