package com.naas.admin_service.core.provider.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.provider.IdentityStoreService;
import com.naas.admin_service.features.users.dto.RoleResponseDto;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.OrganizationRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation cho logic sử dụng Keycloak
 * Sẽ được load vào Spring context khi cấu hình
 * bpm.core.security.permission.use-db = false (hoặc không cấu hình)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "bpm.core.security.permission.use-db", havingValue = "false", matchIfMissing = true)
public class KeycloakIdentityStoreServiceImpl implements IdentityStoreService {

    private final Keycloak keycloak;
    private final RestTemplate restTemplate;

    @Value("${security.keycloak.realm}")
    private String realm;

    @Value("${security.keycloak.base-url}")
    private String baseUrl;

    @Value("${security.keycloak.client-id}")
    private String clientId;

    @Value("${security.keycloak.client-secret}")
    private String clientSecret;

    @Value("${security.keycloak.base-url}")
    private String keycloakBaseUrl;

    @Value("${security.keycloak.admin-realms-path:/admin/realms/}")
    private String adminRealmsPath;

    private static final String REALMS = "/realms/";


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

    @Override
    public List<RoleResponseDto> listRoles() {
        return getRolesResource().list().stream()
                .map(this::toRoleResponseDto)
                .toList();
    }

    @Override
    public RoleResponseDto getRole(String roleName) {
        checkRoleNameExist(roleName);
        return toRoleResponseDto(getRolesResource().get(roleName).toRepresentation());
    }

    @Override
    public void createRole(String roleName, String description) {
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

    @Override
    public void updateRole(String roleName, String description) {
        checkRoleNameExist(roleName);
        RoleRepresentation role = new RoleRepresentation();
        role.setName(roleName);
        if (description != null) {
            role.setDescription(description);
        }
        role.setComposite(false);
        getRolesResource().get(roleName).update(role);
    }

    @Override
    public void deleteRole(String roleName) {
        checkRoleNameExist(roleName);
        getRolesResource().deleteRole(roleName);
    }

    @Override
    public List<RoleResponseDto> getUserRoles(String userId) {
        return keycloak.realm(realm).users().get(userId).roles().realmLevel().listAll().stream()
                .map(r -> new RoleResponseDto(r.getName(), r.getDescription()))
                .toList();
    }

    @Override
    public void assignRoleToUser(String userId, List<String> roleNames) {
        List<RoleRepresentation> rolesToAssign = roleNames.stream()
                .map(roleName -> getRolesResource().get(roleName).toRepresentation())
                .toList();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(rolesToAssign);
    }

    @Override
    public void unAssignRoleFromUser(String userId, List<String> roleNames) {
        List<RoleRepresentation> roles = roleNames.stream()
                .map(roleName -> getRolesResource().get(roleName).toRepresentation())
                .toList();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().remove(roles);
    }

    @Override
    public List<RoleResponseDto> listRolesByGroup(String groupId) {
        return keycloak.realm(realm).groups().group(groupId).roles().realmLevel().listAll().stream()
                .map(r -> new RoleResponseDto(r.getName(), r.getDescription()))
                .toList();
    }

    @Override
    public void addRolesToGroup(String groupId, List<String> roleNames) {
        List<RoleRepresentation> roles = roleNames.stream()
                .map(roleName -> getRolesResource().get(roleName).toRepresentation())
                .toList();
        keycloak.realm(realm).groups().group(groupId).roles().realmLevel().add(roles);
    }

    @Override
    public void removeRolesFromGroup(String groupId, List<String> roleNames) {
        List<RoleRepresentation> roles = roleNames.stream()
                .map(roleName -> getRolesResource().get(roleName).toRepresentation())
                .toList();
        keycloak.realm(realm).groups().group(groupId).roles().realmLevel().remove(roles);
    }

    @Override
    public List<UserRepresentation> listUserByRole(String roleName) {
        checkRoleNameExist(roleName);
        return getRolesResource().get(roleName).getUserMembers();
    }

    private GroupsResource getGroupResource() {
        return keycloak.realm(realm).groups();
    }

    @Override
    public List<GroupRepresentation> listGroups() {
        GroupsResource groupsResource = getGroupResource();
        List<GroupRepresentation> allGroups = new ArrayList<>();
        List<GroupRepresentation> topGroups = groupsResource.groups();
        for (GroupRepresentation group : topGroups) {
            allGroups.add(group);
            getChildGroupsRecursively(group.getId(), allGroups);
        }
        return allGroups;
    }

    private void getChildGroupsRecursively(String parentId,
            List<GroupRepresentation> result) {
        List<GroupRepresentation> subGroups = getChildrenGroupsByParentId(parentId);
        for (GroupRepresentation subGroup : subGroups) {
            result.add(subGroup);
            getChildGroupsRecursively(subGroup.getId(), result);
        }
    }

    private List<GroupRepresentation> getChildrenGroupsByParentId(String parentId) {
        try {
            GroupResource groupResource = getGroupResource().group(parentId);
            GroupRepresentation groupRepresentation = groupResource.toRepresentation();
            List<GroupRepresentation> subGroups = groupRepresentation.getSubGroups();
            return subGroups != null ? subGroups : Collections.emptyList();
        } catch (Exception ex) {
            log.error("Error when fetching group children for parentId {}: {}", parentId, ex.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public GroupRepresentation getGroup(String id) {
        return getGroupResource().group(id).toRepresentation();
    }

    @Override
    public void createGroup(String groupName) {
        List<GroupRepresentation> groupList = listGroups();
        boolean groupNameExist = groupList.stream().anyMatch(group -> group.getName().equals(groupName));
        if (groupNameExist) {
            throw new BusinessException(CommonErrorCode.EXISTS, groupName);
        }
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(groupName);
        getGroupResource().add(groupRepresentation);
    }

    @Override
    public void createChildGroup(String parentGroupId, String childGroupName) {
        List<GroupRepresentation> groupList = listGroups();
        boolean groupNameExist = groupList.stream().anyMatch(group -> group.getName().equals(childGroupName));
        if (groupNameExist) {
            throw new BusinessException(CommonErrorCode.EXISTS, childGroupName);
        }
        GroupRepresentation childGroup = new GroupRepresentation();
        childGroup.setName(childGroupName);
        childGroup.setParentId(parentGroupId);
        GroupResource parentGroupResource = getGroupResource().group(parentGroupId);
        parentGroupResource.subGroup(childGroup);
    }

    @Override
    public void updateGroup(String id, String groupName) {
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(groupName);
        getGroupResource().group(id).update(groupRepresentation);
    }

    @Override
    public void moveGroup(String id, String newParentGroupId) {
        GroupResource sourceGroupResource = getGroupResource().group(id);
        GroupRepresentation sourceGroupRepresentation = sourceGroupResource
                .toRepresentation();
        List<GroupRepresentation> childGroup = getChildrenGroupsByParentId(id);
        List<GroupRepresentation> oldParentChildGroup = getChildrenGroupsByParentId(
                newParentGroupId);
        sourceGroupRepresentation.setId(null);

        GroupResource targetParentResource = getGroupResource()
                .group(newParentGroupId);
        targetParentResource.subGroup(sourceGroupRepresentation);
        List<GroupRepresentation> newParentChildGroup = getChildrenGroupsByParentId(
                newParentGroupId);
        Set<String> oldIds = oldParentChildGroup.stream()
                .map(GroupRepresentation::getId)
                .collect(Collectors.toSet());

        List<GroupRepresentation> newGroups = newParentChildGroup.stream()
                .filter(gr -> !oldIds.contains(gr.getId()))
                .toList();

        if (!childGroup.isEmpty()) {
            for (GroupRepresentation gp : childGroup) {
                this.moveGroup(gp.getId(), newGroups.get(0).getId());
            }
        }
        sourceGroupResource.remove();
    }

    @Override
    public void deleteGroup(String id) {
        getGroupResource().group(id).remove();
    }

    @Override
    public List<UserRepresentation> listUserByGroup(String id) {
        return getGroupResource().group(id).members();
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }

    @Override
    public void addMembersToGroup(List<String> userIds, String id) {
        for (String userId : userIds) {
            getUsersResource().get(userId).joinGroup(id);
        }
    }

    @Override
    public void leaveMembersToGroup(List<String> userIds, String id) {
        for (String userId : userIds) {
            getUsersResource().get(userId).leaveGroup(id);
        }
    }

    @Override
    public UserResource getUser(String userId) {
        return getUsersResource().get(userId);
    }

    @Override
    public void deleteUser(String userId) {
        getUsersResource().delete(userId);
    }

    @Override
    public List<GroupRepresentation> getUserGroups(String userId) {
        return getUser(userId).groups();
    }

    @Override
    public void joinGroups(String userId, List<String> groupIds) {
        UserResource user = getUser(userId);
        groupIds.forEach(user::joinGroup);
    }

    @Override
    public void leaveGroups(String userId, List<String> groupIds) {
        UserResource user = getUser(userId);
        groupIds.forEach(user::leaveGroup);
    }

    @Override
    public ClientsResource getClientsResource() {
        return keycloak.realm(realm).clients();
    }

    @Override
    public ClientResource getClientResource(String id) {
        return keycloak.realm(realm).clients().get(id);
    }

    @Override
    public RealmRepresentation getRealmRepresentation() {
        return keycloak.realm(realm).toRepresentation();
    }

    @Override
    public void updateRealm(RealmRepresentation rep) {
        keycloak.realm(realm).update(rep);
    }

    @Override
    public Map<String, Object> bruteForceUserStatus(String userId) {
        return keycloak.realm(realm).attackDetection().bruteForceUserStatus(userId);
    }

    @Override
    public List<UserRepresentation> searchUsers(String query) {
        return keycloak.realm(realm).users().search(query);
    }

    @Override
    public List<UserRepresentation> searchUsers(String query, int offset, int size) {
        return keycloak.realm(realm).users().search(query, offset, size);
    }

    @Override
    public String createUser(UserRepresentation userRepresentation) {
        try (jakarta.ws.rs.core.Response response = keycloak.realm(realm).users().create(userRepresentation)) {
            if (response.getStatus() == 201) {
                return org.keycloak.admin.client.CreatedResponseUtil.getCreatedId(response);
            }
            String errorBody = "";
            try {
                errorBody = (response.hasEntity()) ? response.readEntity(String.class) : "";
            } catch (Exception ignored) {
                // không xử lý gì
            }
            throw new BusinessException(CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR, errorBody);
        }
    }

    @Override
    public void updateUser(String userId, UserRepresentation userRepresentation) {
        keycloak.realm(realm).users().get(userId).update(userRepresentation);
    }

    @Override
    public void resetUserPassword(String userId, CredentialRepresentation cred) {
        keycloak.realm(realm).users().get(userId).resetPassword(cred);
    }

    @Override
    public List<UserRepresentation> listAllUsersPaged() {
        UsersResource users = keycloak.realm(realm).users();
        List<UserRepresentation> all = new ArrayList<>();
        int first = 0;
        int max = 200;
        boolean hasMore = true;
        while (hasMore) {
            List<UserRepresentation> page;
            try {
                page = users.list(first, max);
            } catch (Exception ex) {
                List<UserRepresentation> once = users.list();
                return once == null ? Collections.emptyList() : once;
            }
            if (page == null || page.isEmpty()) {
                hasMore = false;
            } else {
                all.addAll(page);
                first += max;
                hasMore = page.size() == max;
            }
        }
        return all;
    }

    @Override
    public List<UserRepresentation> searchAllUsersPaged(String filter) {
        UsersResource users = keycloak.realm(realm).users();
        List<UserRepresentation> all = new ArrayList<>();
        int first = 0;
        int max = 200;
        boolean hasMore = true;
        while (hasMore) {
            List<UserRepresentation> page = users.search(filter, first, max);
            if (page == null || page.isEmpty()) {
                hasMore = false;
            } else {
                all.addAll(page);
                first += max;
                hasMore = page.size() == max;
            }
        }
        return all;
    }

    @Override
    public String getRealmAccessTokenKeycloak() {
        return keycloak.tokenManager().getAccessTokenString();
    }

    @Override
    public List<OrganizationRepresentation> searchOrganizations(String query,
            boolean exact, int first, int max) {
        try {
            return keycloak.realm(realm).organizations().search(query, exact, first, max);
        } catch (Exception e) {
            log.error("Failed to search Keycloak organizations: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public void addMemberToOrganization(String orgId, String userId) {
        keycloak.realm(realm).organizations().get(orgId).members().addMember(userId);
    }

    private String adminRealmBaseUrl() {
        return (keycloakBaseUrl != null ? keycloakBaseUrl : baseUrl) + adminRealmsPath + realm;
    }

    @Override
    public List<UserRepresentation> listOrgMembersPaged(String orgId) {
        if (orgId == null || orgId.isBlank())
            return List.of();

        List<UserRepresentation> all = new ArrayList<>();
        String token = getRealmAccessTokenKeycloak();

        int first = 0;
        boolean hasMore = true;
        int kcPageSize = 200;

        while (hasMore) {
            String url = adminRealmBaseUrl()
                    + "/organizations/" + orgId
                    + "/members?first=" + first + "&max=" + kcPageSize;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            ResponseEntity<UserRepresentation[]> resp = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    UserRepresentation[].class);

            UserRepresentation[] body = resp.getBody();
            int len = (body == null) ? 0 : body.length;

            if (len == 0) {
                hasMore = false;
            } else {
                all.addAll(Arrays.asList(body));
                first += kcPageSize;
                hasMore = len == kcPageSize;
            }
        }

        return all;
    }

    @Override
    public boolean isMemberOfOrg(String orgId, String userId) {
        try {
            String token = getRealmAccessTokenKeycloak();

            String url = adminRealmBaseUrl()
                    + "/organizations/" + orgId
                    + "/members/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            ResponseEntity<String> resp = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);

            return resp.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound ex) {
            return false;
        } catch (Exception ex) {
            log.error("Error checking org member orgId={}, userId={}, err={}", orgId, userId, ex.toString());
            return false;
        }
    }

    @Override
    public List<OrganizationRepresentation> fetchOrganizationsByRest(int first,
            int max) {
        try {
            String url = adminRealmBaseUrl() + "/organizations?first=" + first + "&max=" + max;

            String token = getRealmAccessTokenKeycloak();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            ResponseEntity<OrganizationRepresentation[]> resp = restTemplate
                    .exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            OrganizationRepresentation[].class);

            OrganizationRepresentation[] body = resp.getBody();
            return body == null ? List.of() : Arrays.asList(body);
        } catch (HttpClientErrorException.Forbidden ex) {
            log.error("FORBIDDEN when listing organizations. Need service-account roles. err={}", ex.getMessage());
            return List.of();
        } catch (Exception ex) {
            log.error("Error fetching organizations by REST: {}", ex.toString());
            return List.of();
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> exchangeAuthToken(
            MultiValueMap<String, String> form) {
        String authUrl = baseUrl + REALMS + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(
                form, headers);

        return restTemplate.exchange(
                authUrl,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }

    @Override
    public ResponseEntity<Map<String, Object>> getUserInfo(String token) {
        String url = baseUrl + REALMS + realm + "/protocol/openid-connect/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }

    @Override
    public void verifyCurrentPassword(String username, String currentPassword) {
        String tokenUrl = baseUrl + REALMS + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username);
        body.add("password", currentPassword);

        try {
            ResponseEntity<String> res = restTemplate.postForEntity(tokenUrl,
                    new HttpEntity<>(body, headers),
                    String.class);
            if (!res.getStatusCode().is2xxSuccessful()) {
                throw new BusinessException(ErrorCode.UNAUTHORIZED, "Mật khẩu hiện tại không đúng");
            }
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Mật khẩu hiện tại không đúng");
        }
    }

    @Override
    public RolesResource getRolesResource() {
        return keycloak.realm(realm).roles();
    }
}
