package com.ngvgroup.bpm.core.security.oauth2;

import com.ngvgroup.bpm.core.autoconfigure.BpmCoreProperties;
import com.ngvgroup.bpm.core.common.dto.OAuth2TokenResponse;
import com.ngvgroup.bpm.core.security.config.KeycloakProperties;
import com.ngvgroup.bpm.core.security.oauth2.feign.OAuth2TokenFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2ClientCredentialsService {

    private final OAuth2TokenFeignClient tokenFeignClient;
    private final BpmCoreProperties properties;
    private final ObjectProvider<KeycloakProperties> keycloakPropertiesProvider;

    private OAuth2TokenResponse cachedToken;
    private long tokenExpireTime = 0;

    public synchronized String getAccessToken() {
        long now = System.currentTimeMillis();

        boolean cacheEnabled = properties.getOauth2() == null || properties.getOauth2().isCacheToken();
        if (cacheEnabled && cachedToken != null && now < tokenExpireTime) {
            return cachedToken.getAccessToken();
        }

        String clientId = (properties.getOauth2() != null) ? properties.getOauth2().getClientId() : null;
        String clientSecret = (properties.getOauth2() != null) ? properties.getOauth2().getClientSecret() : null;

        KeycloakProperties kc = keycloakPropertiesProvider.getIfAvailable();
        if ((clientId == null || clientId.isBlank()) && kc != null) {
            clientId = kc.getClientId();
        }
        if ((clientSecret == null || clientSecret.isBlank()) && kc != null) {
            clientSecret = kc.getClientSecret();
        }
        if (clientId == null || clientId.isBlank() || clientSecret == null || clientSecret.isBlank()) {
            throw new IllegalStateException(
                    "Missing client credentials. Set security.keycloak.client-id/client-secret (recommended) " +
                    "or bpm.core.oauth2.client-id/client-secret (legacy).");
        }

        Map<String, ?> formData = Map.of(
                "grant_type", "client_credentials",
                "client_id", clientId,
                "client_secret", clientSecret
        );

        OAuth2TokenResponse tokenResponse = tokenFeignClient.getToken(formData);
        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new RuntimeException("Không lấy được access token từ Authorization Server");
        }

        cachedToken = tokenResponse;
        tokenExpireTime = now + (Math.max(10, tokenResponse.getExpiresIn() - 30)) * 1000L;
        return cachedToken.getAccessToken();
    }
}
