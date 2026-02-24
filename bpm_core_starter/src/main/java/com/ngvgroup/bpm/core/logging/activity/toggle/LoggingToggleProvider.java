package com.ngvgroup.bpm.core.logging.activity.toggle;

public interface LoggingToggleProvider {

    /**
     * Bật/tắt Activity Log (COM_INF_LOG_ACTIVITY)
     */
    boolean isActivityLogEnabled();

    /**
     * Bật/tắt Audit Log (COM_INF_LOG_AUDIT)
     * Nếu bạn muốn “audit phải bật khi activity bật” thì có thể implement = isActivityLogEnabled()
     */
    default boolean isAuditLogEnabled() {
        return isActivityLogEnabled();
    }
}
