package com.ngvgroup.bpm.core.autoconfigure;

import com.ngvgroup.bpm.core.persistence.config.ServiceCodeProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class BpmCoreServiceCodeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServiceCodeProvider serviceCodeProvider(
            @Value("${spring.application.name:unknown-service}") String serviceName
    ) {
        return new ServiceCodeProvider(serviceName);
    }
}
