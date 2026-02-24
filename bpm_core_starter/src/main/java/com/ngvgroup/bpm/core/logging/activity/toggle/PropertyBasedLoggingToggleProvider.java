package com.ngvgroup.bpm.core.logging.activity.toggle;

import com.ngvgroup.bpm.core.autoconfigure.BpmCoreProperties;

public class PropertyBasedLoggingToggleProvider implements LoggingToggleProvider {

    private final BpmCoreProperties props;

    public PropertyBasedLoggingToggleProvider(BpmCoreProperties props) {
        this.props = props;
    }

    @Override
    public boolean isActivityLogEnabled() {
        // vẫn dùng cờ cũ, hoặc bạn fix cứng true cũng được
        return props.isActivityLogEnabled();
    }

    @Override
    public boolean isAuditLogEnabled() {
        // Nếu muốn: audit chỉ chạy khi activity bật
        return props.isActivityLogEnabled();
    }
}