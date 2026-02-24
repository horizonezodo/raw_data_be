package com.ngvgroup.bpm.core.logging.activity.toggle;

import com.ngvgroup.bpm.core.logging.activity.dto.LoggingToggleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;

@Slf4j
public class RemoteLoggingToggleProvider implements LoggingToggleProvider {

    private final RestTemplate restTemplate;
    private final LoggingToggleProperties props;
    private final String serviceName;

    private volatile CacheEntry cache;

    public RemoteLoggingToggleProvider(RestTemplate restTemplate,
                                       LoggingToggleProperties props,
                                       String serviceName) {
        this.restTemplate = restTemplate;
        this.props = props;
        this.serviceName = serviceName;
    }

    private static class CacheEntry {
        final boolean activity;
        final boolean audit;
        final long expireAtEpochMs;

        CacheEntry(boolean activity, boolean audit, long expireAtEpochMs) {
            this.activity = activity;
            this.audit = audit;
            this.expireAtEpochMs = expireAtEpochMs;
        }

        boolean isExpired() {
            return Instant.now().toEpochMilli() > expireAtEpochMs;
        }
    }

    private CacheEntry getOrFetch() {
        CacheEntry current = cache;
        if (current != null && !current.isExpired()) {
            return current;
        }

        // Fallback mặc định nếu không gọi được remote:
        boolean act = true;  // default: bật activity
        boolean aud = true;  // default: bật audit

        // Nếu không cấu hình remote hoặc disabled -> dùng default (true/true)
        if (!props.isEnabled()
                || props.getRemoteBaseUrl() == null
                || props.getRemoteBaseUrl().isBlank()) {

            cache = new CacheEntry(
                    act,
                    aud,
                    Instant.now().toEpochMilli() + props.getCacheTtlSeconds() * 1000
            );
            return cache;
        }

        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(props.getRemoteBaseUrl())
                    .queryParam("serviceName", serviceName)
                    .toUriString();

            LoggingToggleResponse resp =
                    restTemplate.getForObject(url, LoggingToggleResponse.class);

            if (resp != null && resp.getData() != null) {
                act = resp.getData().isActivityLogEnabled();
                aud = resp.getData().isAuditLogEnabled();
            } else {
                log.warn("[LoggingToggle] Response null or data=null, fallback default=true");
            }
        } catch (Exception ex) {
            log.warn("[LoggingToggle] Call remote config failed, use default=true. reason={}", ex.getMessage());
        }

        cache = new CacheEntry(
                act,
                aud,
                Instant.now().toEpochMilli() + props.getCacheTtlSeconds() * 1000
        );
        return cache;
    }

    @Override
    public boolean isActivityLogEnabled() {
        return getOrFetch().activity;
    }

    @Override
    public boolean isAuditLogEnabled() {
        return getOrFetch().audit;
    }
}
