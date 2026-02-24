package com.ngvgroup.bpm.core.autoconfigure;

import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.security.oauth2.feign.MultitenantFeignHeaderInterceptor;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(RequestInterceptor.class)
@ConditionalOnProperty(prefix = "multitenancy", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(MultitenancyProperties.class)
public class BpmCoreFeignMultitenantAutoConfiguration {

    @Bean(name = "multitenantFeignHeaderInterceptor")
    public RequestInterceptor multitenantFeignHeaderInterceptor(MultitenancyProperties props) {
        return new MultitenantFeignHeaderInterceptor(props);
    }
}
