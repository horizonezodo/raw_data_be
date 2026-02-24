package com.ngvgroup.bpm.core.liquibase;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "bpm.liquibase.tenant")
public class TenantLiquibaseProperties {

    /** Bật/tắt tính năng migrate theo tenant */
    private boolean enabled = true;

    /** Master changelog (mỗi service đặt trong resources/db) */
    private String changeLog = "classpath:db/db.changelog-master.xml";

    /** Có chạy migrate lúc startup không */
    private boolean migrateOnStartup = true;

    private Map<String, String> schemaTargets = new HashMap<>();

    private boolean debugConnection = false;
}
