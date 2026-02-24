package ngvgroup.com.bpm.core.config;

import java.util.Map;

import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaRestAuthConfig {

    @Bean
    public FilterRegistrationBean<ProcessEngineAuthenticationFilter> processEngineAuthenticationFilter() {
        FilterRegistrationBean<ProcessEngineAuthenticationFilter> reg = new FilterRegistrationBean<>();
        ProcessEngineAuthenticationFilter filter = new ProcessEngineAuthenticationFilter();

        reg.setFilter(filter);
        reg.addUrlPatterns("/api/engine-rest/*");

        reg.setInitParameters(Map.of(
                "authentication-provider",
                "org.camunda.bpm.engine.rest.security.auth.impl.ContainerBasedAuthenticationProvider"));

        reg.setOrder(101); // sau Spring Security
        return reg;
    }
}
