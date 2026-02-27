package com.naas.admin_service.features.me.service;

/**
 * System-wide login mode controlled by COM_CFG_SETTING (Layout_Login_SSO).
 *  - settingValue = 1 => ONLY allow SSO login
 *  - else           => ONLY allow username/password login
 */
public interface SystemLoginModeService {
    boolean isSsoOnly();
    default boolean isPasswordOnly() {
        return !isSsoOnly();
    }
}
