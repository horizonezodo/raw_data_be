package com.ngvgroup.bpm.core.autoconfigure;

import com.ngvgroup.bpm.core.security.config.KeycloakProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;

@AutoConfiguration(before = BpmCoreSecurityAutoConfiguration.class)
@ConditionalOnClass(JwtDecoder.class)
@EnableConfigurationProperties(KeycloakProperties.class)
public class BpmCoreKeycloakAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(JwtDecoder.class)
    public JwtDecoder bpmJwtDecoder(KeycloakProperties kc) {
        String issuer = resolveIssuerUri(kc);
        JwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuer);

        // add default issuer validation (defensive)
        if (decoder instanceof org.springframework.security.oauth2.jwt.NimbusJwtDecoder nimbus) {
            OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
            nimbus.setJwtValidator(withIssuer);
            return nimbus;
        }
        return decoder;
    }

    private static String resolveIssuerUri(KeycloakProperties kc) {
        if (kc.getIssuerUri() != null && !kc.getIssuerUri().isBlank()) {
            return kc.getIssuerUri().trim();
        }
        if (kc.getBaseUrl() == null || kc.getBaseUrl().isBlank()
                || kc.getRealm() == null || kc.getRealm().isBlank()) {
            throw new IllegalStateException(
                    "Missing Keycloak config. Set either security.keycloak.issuer-uri " +
                    "or security.keycloak.base-url + security.keycloak.realm");
        }
        String base = kc.getBaseUrl().trim().replaceAll("/+$", "");
        String realm = kc.getRealm().trim();
        return base + "/realms/" + realm;
    }
}
