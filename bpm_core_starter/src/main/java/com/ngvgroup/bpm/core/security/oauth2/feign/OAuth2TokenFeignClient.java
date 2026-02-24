package com.ngvgroup.bpm.core.security.oauth2.feign;

import com.ngvgroup.bpm.core.common.dto.OAuth2TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "oauth2TokenFeignClient", url = "${security.keycloak.token-uri:${bpm.core.oauth2.token-uri}}")
public interface OAuth2TokenFeignClient {
    @PostMapping(consumes = "application/x-www-form-urlencoded")
    OAuth2TokenResponse getToken(@RequestBody Map<String, ?> formData);
} 