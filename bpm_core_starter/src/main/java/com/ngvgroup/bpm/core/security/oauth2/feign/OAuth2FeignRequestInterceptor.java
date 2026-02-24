package com.ngvgroup.bpm.core.security.oauth2.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.ngvgroup.bpm.core.security.oauth2.OAuth2ClientCredentialsService;

public record OAuth2FeignRequestInterceptor(OAuth2ClientCredentialsService oAuth2ClientCredentialsService) implements RequestInterceptor {
    @Autowired
    public OAuth2FeignRequestInterceptor {
    }

    @Override
    public void apply(RequestTemplate template) {
        String token = oAuth2ClientCredentialsService.getAccessToken();
        template.header("Authorization", "Bearer " + token);
    }
} 