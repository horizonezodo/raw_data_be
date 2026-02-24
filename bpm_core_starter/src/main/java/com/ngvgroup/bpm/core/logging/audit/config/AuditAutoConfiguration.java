package com.ngvgroup.bpm.core.logging.audit.config;

import com.ngvgroup.bpm.core.security.audit.AuditorConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(AuditorConfig.class)
public class AuditAutoConfiguration {
    // Chỉ cần import AuditorConfig để Spring Boot tự kích hoạt auditing
}