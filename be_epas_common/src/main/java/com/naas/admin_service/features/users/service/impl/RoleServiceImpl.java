package com.naas.admin_service.features.users.service.impl;

import com.naas.admin_service.core.provider.IdentityStoreService;
import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.naas.admin_service.features.users.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final IdentityStoreService identityStoreService;

    @Override
    public List<RoleResponseDto> listRoles() {
        return identityStoreService.listRoles();
    }

    @Override
    public RoleResponseDto getRole(String roleName) {
        return identityStoreService.getRole(roleName);
    }

    @Override
    public void createRole(String roleName, String description) {
        identityStoreService.createRole(roleName, description);
    }

    @Override
    public void updateRole(String roleName, String description) {
        identityStoreService.updateRole(roleName, description);
    }

    @Override
    public void deleteRole(String roleName) {
        identityStoreService.deleteRole(roleName);
    }

    @Override
    public List<UserRepresentation> listUserByRole(String roleName) {
        return identityStoreService.listUserByRole(roleName);
    }

    // Helpers like toRoleResponseDto etc. can be removed as they are already in the
    // Keycloak provider
}
