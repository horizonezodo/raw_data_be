package com.naas.admin_service.features.permission.service;

import com.naas.admin_service.features.permission.dto.ComCfgPermissionDto;

import java.util.List;

public interface PermissionService {
    void createPermission(ComCfgPermissionDto permissionDto);
    List<ComCfgPermissionDto> getAllPermissions();
    ComCfgPermissionDto getPermissionByCode(String code);
    void updatePermission(String code, ComCfgPermissionDto permissionDto);
    void deletePermission(String code);
    void assignRolesToUser(String userId, List<String> roleNames);
    void unAssignRolesFromUser(String userId, List<String> roleNames);
    void addRolesToGroup(String groupName, List<String> roleNames);
    void removeRolesFromGroup(String groupName, List<String> roleNames);
}
