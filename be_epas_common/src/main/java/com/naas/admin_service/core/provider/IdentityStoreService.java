package com.naas.admin_service.core.provider;

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
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import com.naas.admin_service.features.users.dto.RoleResponseDto;

/**
 * Interface chung để xử lý logic thay phiên giữa Keycloak và Database.
 * Các service ở features/ sẽ gọi trực tiếp interface này thay vì if-else
 * isUsingDb.
 */
public interface IdentityStoreService {

        List<RoleResponseDto> listRoles();

        RoleResponseDto getRole(String roleName);

        void createRole(String roleName, String description);

        void updateRole(String roleName, String description);

        void deleteRole(String roleName);

        List<RoleResponseDto> getUserRoles(String userId);

        void assignRoleToUser(String userId, List<String> roleNames);

        void unAssignRoleFromUser(String userId, List<String> roleNames);

        List<RoleResponseDto> listRolesByGroup(String groupId);

        void addRolesToGroup(String groupId, List<String> roleNames);

        void removeRolesFromGroup(String groupId, List<String> roleNames);

        List<UserRepresentation> listUserByRole(String roleName);

        List<GroupRepresentation> listGroups();

        GroupRepresentation getGroup(String id);

        void createGroup(String groupName);

        void createChildGroup(String parentGroupId, String childGroupName);

        void updateGroup(String id, String groupName);

        void moveGroup(String id, String newParentGroupId);

        void deleteGroup(String id);

        List<UserRepresentation> listUserByGroup(String id);

        void addMembersToGroup(List<String> userIds, String id);

        void leaveMembersToGroup(List<String> userIds, String id);

        UserResource getUser(String userId);

        void deleteUser(String userId);

        List<GroupRepresentation> getUserGroups(String userId);

        void joinGroups(String userId, List<String> groupIds);

        void leaveGroups(String userId, List<String> groupIds);

        ClientsResource getClientsResource();

        ClientResource getClientResource(String id);

        RealmRepresentation getRealmRepresentation();

        void updateRealm(RealmRepresentation rep);

        Map<String, Object> bruteForceUserStatus(String userId);

        List<UserRepresentation> searchUsers(String query);

        List<UserRepresentation> searchUsers(String query, int offset, int size);

        String createUser(UserRepresentation userRepresentation);

        void updateUser(String userId, UserRepresentation userRepresentation);

        void resetUserPassword(String userId, CredentialRepresentation cred);

        List<UserRepresentation> listAllUsersPaged();

        List<UserRepresentation> searchAllUsersPaged(String filter);

        String getRealmAccessTokenKeycloak();

        List<OrganizationRepresentation> searchOrganizations(String query, boolean exact,
                        int first, int max);

        void addMemberToOrganization(String orgId, String userId);

        List<UserRepresentation> listOrgMembersPaged(String orgId);

        boolean isMemberOfOrg(String orgId, String userId);

        List<OrganizationRepresentation> fetchOrganizationsByRest(int first, int max);

        ResponseEntity<Map<String, Object>> exchangeAuthToken(
                        MultiValueMap<String, String> form);

        ResponseEntity<Map<String, Object>> getUserInfo(String token);

        void verifyCurrentPassword(String username, String currentPassword);

        RolesResource getRolesResource();
}
