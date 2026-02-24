package com.ngv.zns_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZnsConfig {

    @Value("${fe.url}")
    private String url;

    public String getUrl() {
        return url;
    }
}
