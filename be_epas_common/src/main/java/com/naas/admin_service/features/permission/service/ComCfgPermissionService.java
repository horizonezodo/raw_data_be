package com.naas.admin_service.features.permission.service;

import java.util.Set;

public interface ComCfgPermissionService {
    /**
     * Lấy các permission_code được gán trực tiếp cho USER (preferred_username).
     */
    Set<String> findPermissionCodesByUserId(String userId);

    /**
     * Lấy các permission_code được gán cho GROUP.
     */
    Set<String> findPermissionCodesByGroupName(String groupName);
}