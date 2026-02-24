package com.ngvgroup.bpm.core.logging.activity.toggle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "bpm.core.logging-toggle")
public class LoggingToggleProperties {

    /**
     * Bật/tắt cơ chế toggle nói chung (nếu false thì coi như luôn bật log).
     */
    private boolean enabled = true;

    /**
     * Base URL của common / admin-service để hỏi trạng thái log.
     * Ví dụ: http://admin-service:8300/internal/logging-toggle
     */
    private String remoteBaseUrl;

    /**
     * TTL cache (giây) – để không gọi API mỗi request.
     */
    private long cacheTtlSeconds = 30;

}
