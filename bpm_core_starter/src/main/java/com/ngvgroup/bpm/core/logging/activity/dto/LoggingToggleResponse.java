package com.ngvgroup.bpm.core.logging.activity.dto;

import lombok.Data;

@Data
public class LoggingToggleResponse {
    private int status;
    private String message;
    private LoggingToggleData data;

    @Data
    public static class LoggingToggleData {
        private boolean activityLogEnabled;
        private boolean auditLogEnabled;
    }
}
