package com.naas.admin_service.core.config;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessFeignConfig {

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }
}

