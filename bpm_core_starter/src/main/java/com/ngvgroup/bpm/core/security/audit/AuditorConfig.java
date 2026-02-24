package com.ngvgroup.bpm.core.security.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditorConfig {

    @Bean
    public KeycloakAuditorAware auditorAware() {
        return new KeycloakAuditorAware();
    }
}