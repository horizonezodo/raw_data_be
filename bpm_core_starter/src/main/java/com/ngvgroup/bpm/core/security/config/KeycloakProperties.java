package com.ngvgroup.bpm.core.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.keycloak")
public class KeycloakProperties {
    private String issuerUri;
    private String baseUrl;
    private String realm;

    private String clientId;
    private String clientSecret;
    private String redirectUri;

    private String adminRealmsPath = "/admin/realms/";
    private String scopeOpenidEmailProfile;

}
