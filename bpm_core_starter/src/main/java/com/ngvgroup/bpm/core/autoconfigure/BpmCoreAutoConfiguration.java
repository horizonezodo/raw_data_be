package com.ngvgroup.bpm.core.autoconfigure;

import com.ngvgroup.bpm.core.common.exception.GlobalExceptionHandler;
import com.ngvgroup.bpm.core.logging.activity.aspect.ActivityLogAspect;
import com.ngvgroup.bpm.core.logging.activity.toggle.LoggingToggleProperties;
import com.ngvgroup.bpm.core.logging.activity.toggle.LoggingToggleProvider;
import com.ngvgroup.bpm.core.logging.activity.toggle.RemoteLoggingToggleProvider;
import com.ngvgroup.bpm.core.security.UsernameNormalizer;
import com.ngvgroup.bpm.core.security.config.KeycloakProperties;
import com.ngvgroup.bpm.core.web.interceptor.RequestIdInterceptor;
import com.ngvgroup.bpm.core.persistence.config.StoredProcedureProperties;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.StoredProcedureService;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.impl.StoredProcedureServiceImpl;
import com.ngvgroup.bpm.core.security.oauth2.OAuth2ClientCredentialsService;
import com.ngvgroup.bpm.core.security.oauth2.feign.OAuth2TokenFeignClient;
import com.ngvgroup.bpm.core.security.oauth2.feign.OAuth2FeignRequestInterceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.ngvgroup.bpm.core.logging.kafka.service.LoggingKafkaProducerService;

import javax.sql.DataSource;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(
        {
                StoredProcedureProperties.class,
                BpmCoreProperties.class,
                LoggingToggleProperties.class,
        }
)
@EnableFeignClients(basePackages = {"com.ngvgroup.bpm.core.security.oauth2.feign"})
@ComponentScan(basePackages = {
        "com.ngvgroup.bpm.core.logging.kafka",
        "com.ngvgroup.bpm.core.common.excel",
        "com.ngvgroup.bpm.core.common.spring"
})
public class BpmCoreAutoConfiguration implements WebMvcConfigurer {
    
    @Bean
    @ConditionalOnMissingBean
    public RequestIdInterceptor requestIdInterceptor() {
        return new RequestIdInterceptor();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DataSource.class)
    public StoredProcedureService storedProcedureService(DataSource dataSource, StoredProcedureProperties properties) {
        return new StoredProcedureServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2ClientCredentialsService oAuth2ClientCredentialsService(
            BpmCoreProperties properties,
            OAuth2TokenFeignClient tokenFeignClient,
            ObjectProvider<KeycloakProperties> keycloakPropertiesProvider
    ) {
        return new OAuth2ClientCredentialsService(tokenFeignClient, properties, keycloakPropertiesProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor(
            OAuth2ClientCredentialsService oAuth2ClientCredentialsService
    ) {
        return new OAuth2FeignRequestInterceptor(oAuth2ClientCredentialsService);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoggingToggleProvider loggingToggleProvider(
            LoggingToggleProperties toggleProps,
            RestTemplateBuilder restTemplateBuilder,
            @Value("${spring.application.name:unknown-service}") String appName
    ) {
        if (toggleProps.isEnabled()
                && toggleProps.getRemoteBaseUrl() != null
                && !toggleProps.getRemoteBaseUrl().isBlank()) {

            RestTemplate rt = restTemplateBuilder
                    .setConnectTimeout(java.time.Duration.ofMillis(1000))
                    .setReadTimeout(java.time.Duration.ofMillis(1000))
                    .build();

            return new RemoteLoggingToggleProvider(rt, toggleProps, appName);
        }

        return new LoggingToggleProvider() {
            @Override
            public boolean isActivityLogEnabled() {
                return true;
            }

            @Override
            public boolean isAuditLogEnabled() {
                return true;
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "bpm.core",
            name = "activity-log-enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public ActivityLogAspect activityLogAspect(LoggingKafkaProducerService loggingKafkaProducerService,
                                               LoggingToggleProvider toggleProvider,
                                               @Value("${spring.application.name:unknown-service}") String appName) {
        return new ActivityLogAspect(loggingKafkaProducerService, toggleProvider, appName);
    }
    
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(requestIdInterceptor())
               .addPathPatterns("/**")
               .excludePathPatterns("/error");
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "multitenancy",
            name = "enabled",
            havingValue = "false",
            matchIfMissing = true
    )
    public UsernameNormalizer usernameNormalizer() {
        return username -> username; // single-tenant: không đụng tenant
    }
} 