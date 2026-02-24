package com.naas.admin_service.features.client.service;

import com.naas.admin_service.features.client.dto.ClientRequest;
import com.naas.admin_service.features.client.dto.RoleDto;

import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface ClientService {
    List<ClientRepresentation> listClients();

    ClientRepresentation getClient(String id);

    void createClient(ClientRequest request) ;

    void updateClient(String id, ClientRequest request);

    void deleteClient(String id);

    List<RoleRepresentation> listClientRole(String id);

    RoleRepresentation getClientRole(String containerId, String roleName);

    void createClientRole(String id, String roleName, String description);

    void updateClientRole(String id, String roleName, String description);

    void deleteClientRole(String id, String roleName);

    List<UserRepresentation> listUserByClientRole(String clientId, String roleName);
    List<RoleDto> listAllClientRoles();

    void assignClientRoleToUser(String clientId, String userId, String roleName);

    void assignServiceAccountRoles(String clientId, List<String> roleNames);

    void unAssignServiceAccountRoles(String clientId, List<String> roleNames);
    List<RoleRepresentation> getServiceAccountRoles(String clientId);



}
