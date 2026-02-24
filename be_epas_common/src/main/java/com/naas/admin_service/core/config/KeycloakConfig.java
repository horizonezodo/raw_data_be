package com.naas.admin_service.core.config;

import com.ngvgroup.bpm.core.security.config.KeycloakProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KeycloakConfig {

    private final KeycloakProperties keycloakProperties;

    public KeycloakConfig(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .clientSecret(keycloakProperties.getClientSecret())
                .clientId(keycloakProperties.getClientId())
                .grantType("client_credentials")
                .realm(keycloakProperties.getRealm())
                .serverUrl(keycloakProperties.getBaseUrl())
                .scope("openid")
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
