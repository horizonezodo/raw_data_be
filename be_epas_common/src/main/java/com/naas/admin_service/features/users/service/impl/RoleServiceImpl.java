package com.naas.admin_service.features.users.service.impl;


import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.permission.dto.ComCfgPermissionDto;
import com.naas.admin_service.features.permission.service.PermissionService;
import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.naas.admin_service.features.users.service.RoleService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final PermissionService permissionService;
    private final Keycloak keycloak;

    @Value("${security.keycloak.realm}")
    private String realm;

    @Value("${bpm.core.security.permission.use-db:false}")
    private boolean isUsingDb;

    @Override
    public List<RoleResponseDto> listRoles() {
        if (isUsingDb) {
            return listRolesFromDb();
        } else {
            return listRolesFromKeycloak();
        }
    }

    @Override
    public RoleResponseDto getRole(String roleName) {
        if (isUsingDb) {
            return getRoleFromDb(roleName);
        } else {
            return getRoleFromKeycloak(roleName);
        }
    }

    @Override
    public void createRole(String roleName, String description) {
        if (isUsingDb) {
            createRoleInDb(roleName, description);
        } else {
            createRoleInKeycloak(roleName, description);
        }
    }

    @Override
    public void updateRole(String roleName, String description) {
        if (isUsingDb) {
            updateRoleInDb(roleName, description);
        } else {
            updateRoleInKeycloak(roleName, description);
        }
    }

    @Override
    public void deleteRole(String roleName) {
        if (isUsingDb) {
            deleteRoleInDb(roleName);
        } else {
            deleteRoleInKeycloak(roleName);
        }
    }

    @Override
    public List<UserRepresentation> listUserByRole(String roleName) {
        this.checkRoleNameExist(roleName);
        RoleResource role = getRolesResource().get(roleName);
        return role.getUserMembers();
    }

    private List<RoleResponseDto> listRolesFromDb() {
        return permissionService.getAllPermissions().stream()
                .map(p -> new RoleResponseDto(p.getCode(), p.getName()))
                .toList();
    }

    private List<RoleResponseDto> listRolesFromKeycloak() {
        return getRolesResource().list().stream()
                .map(this::toRoleResponseDto)
                .toList();
    }

    private RoleResponseDto getRoleFromDb(String roleName) {
        ComCfgPermissionDto permission = permissionService.getPermissionByCode(roleName);
        return new RoleResponseDto(permission.getCode(), permission.getName());
    }

    private RoleResponseDto getRoleFromKeycloak(String roleName) {
        checkRoleNameExist(roleName);
        return toRoleResponseDto(getRolesResource().get(roleName).toRepresentation());
    }

    private void createRoleInDb(String roleName, String description) {
        ComCfgPermissionDto permissionDto = new ComCfgPermissionDto();
        permissionDto.setCode(roleName);
        permissionDto.setName(description);
        permissionService.createPermission(permissionDto);
    }

    private void createRoleInKeycloak(String roleName, String description) {
        List<RoleRepresentation> roleList = getRolesResource().list();
        boolean roleNameExist = roleList.stream().anyMatch(role -> role.getName().equals(roleName));
        if (roleNameExist) {
            throw new BusinessException(CommonErrorCode.EXISTS, roleName);
        }
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleName);
        if (description != null) {
            role.setDescription(description);
        }
        role.setComposite(false);
        getRolesResource().create(role);
    }

    private void updateRoleInDb(String roleName, String description) {
        ComCfgPermissionDto permissionDto = new ComCfgPermissionDto();
        permissionDto.setName(description);
        permissionService.updatePermission(roleName, permissionDto);
    }

    private void updateRoleInKeycloak(String roleName, String description) {
        checkRoleNameExist(roleName);
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleName);
        if (description != null) {
            role.setDescription(description);
        }
        role.setComposite(false);
        getRolesResource().get(roleName).update(role);
    }

    private void deleteRoleInDb(String roleName) {
        permissionService.deletePermission(roleName);
    }

    private void deleteRoleInKeycloak(String roleName) {
        checkRoleNameExist(roleName);
        getRolesResource().deleteRole(roleName);
    }

    private RolesResource getRolesResource() {
        return keycloak.realm(realm).roles();
    }

    private void checkRoleNameExist(String roleName) {
        try {
            getRolesResource().get(roleName).toRepresentation();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NOT_FOUND, roleName);
        }
    }

    private RoleResponseDto toRoleResponseDto(RoleRepresentation roleRepresentation) {
        RoleResponseDto dto = new RoleResponseDto();
        dto.setName(roleRepresentation.getName());
        dto.setDescription(roleRepresentation.getDescription());
        return dto;
    }
}
