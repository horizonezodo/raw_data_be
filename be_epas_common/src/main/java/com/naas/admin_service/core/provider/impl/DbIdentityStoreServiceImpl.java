package com.naas.admin_service.core.provider.impl;

import com.naas.admin_service.core.provider.IdentityStoreService;
import com.naas.admin_service.features.permission.dto.ComCfgPermissionDto;
import com.naas.admin_service.features.permission.repository.ComCfgPermissionMapRepository;
import com.naas.admin_service.features.permission.service.PermissionService;
import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.OrganizationRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 * Implementation cho logic sử dụng Database
 * Sẽ được load vào Spring context khi cấu hình
 * bpm.core.security.permission.use-db = true
 */
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "bpm.core.security.permission.use-db", havingValue = "true")
public class DbIdentityStoreServiceImpl implements IdentityStoreService {

    private final PermissionService permissionService;
    private final ComCfgPermissionMapRepository permissionMapRepository;

    @Override
    public List<RoleResponseDto> listRoles() {
        return permissionService.getAllPermissions().stream()
                .map(p -> new RoleResponseDto(p.getCode(), p.getName()))
                .toList();
    }

    @Override
    public RoleResponseDto getRole(String roleName) {
        ComCfgPermissionDto permission = permissionService.getPermissionByCode(roleName);
        return new RoleResponseDto(permission.getCode(), permission.getName());
    }

    @Override
    public void createRole(String roleName, String description) {
        ComCfgPermissionDto permissionDto = new ComCfgPermissionDto();
        permissionDto.setCode(roleName);
        permissionDto.setName(description);
        permissionService.createPermission(permissionDto);
    }

    @Override
    public void updateRole(String roleName, String description) {
        ComCfgPermissionDto permissionDto = new ComCfgPermissionDto();
        permissionDto.setName(description);
        permissionService.updatePermission(roleName, permissionDto);
    }

    @Override
    public void deleteRole(String roleName) {
        permissionService.deletePermission(roleName);
    }

    @Override
    public List<RoleResponseDto> getUserRoles(String userId) {
        return permissionMapRepository.findPermissionInfoByUserId(userId);
    }

    @Override
    public void assignRoleToUser(String userId, List<String> roleNames) {
        permissionService.assignRolesToUser(userId, roleNames);
    }

    @Override
    public void unAssignRoleFromUser(String userId, List<String> roleNames) {
        permissionService.unAssignRolesFromUser(userId, roleNames);
    }

    @Override
    public List<RoleResponseDto> listRolesByGroup(String groupId) {
        String groupName = getGroup(groupId).getName();
        return permissionMapRepository.findPermissionInfoByGroupName(groupName);
    }

    @Override
    public void addRolesToGroup(String groupId, List<String> roleNames) {
        String groupName = getGroup(groupId).getName();
        permissionService.addRolesToGroup(groupName, roleNames);
    }

    @Override
    public void removeRolesFromGroup(String groupId, List<String> roleNames) {
        String groupName = getGroup(groupId).getName();
        permissionService.removeRolesFromGroup(groupName, roleNames);
    }

    @Override
    public List<UserRepresentation> listUserByRole(String roleName) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<GroupRepresentation> listGroups() {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public GroupRepresentation getGroup(String id) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void createGroup(String groupName) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void createChildGroup(String parentGroupId, String childGroupName) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void updateGroup(String id, String groupName) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void moveGroup(String id, String newParentGroupId) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void deleteGroup(String id) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<UserRepresentation> listUserByGroup(String id) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void addMembersToGroup(List<String> userIds, String id) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void leaveMembersToGroup(List<String> userIds, String id) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public UserResource getUser(String userId) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void deleteUser(String userId) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<GroupRepresentation> getUserGroups(String userId) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void joinGroups(String userId, List<String> groupIds) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void leaveGroups(String userId, List<String> groupIds) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ClientsResource getClientsResource() {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ClientResource getClientResource(String id) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public RealmRepresentation getRealmRepresentation() {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void updateRealm(RealmRepresentation rep) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Map<String, Object> bruteForceUserStatus(String userId) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<UserRepresentation> searchUsers(String query) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<UserRepresentation> searchUsers(String query, int offset, int size) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public String createUser(UserRepresentation userRepresentation) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void updateUser(String userId, UserRepresentation userRepresentation) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void resetUserPassword(String userId, CredentialRepresentation cred) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<UserRepresentation> listAllUsersPaged() {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<UserRepresentation> searchAllUsersPaged(String filter) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public String getRealmAccessTokenKeycloak() {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<OrganizationRepresentation> searchOrganizations(String query,
            boolean exact, int first, int max) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void addMemberToOrganization(String orgId, String userId) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<UserRepresentation> listOrgMembersPaged(String orgId) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public boolean isMemberOfOrg(String orgId, String userId) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<OrganizationRepresentation> fetchOrganizationsByRest(int first,
            int max) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, Object>> exchangeAuthToken(
            MultiValueMap<String, String> form) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Map<String, Object>> getUserInfo(String token) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public void verifyCurrentPassword(String username, String currentPassword) {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Override
    public RolesResource getRolesResource() {
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
