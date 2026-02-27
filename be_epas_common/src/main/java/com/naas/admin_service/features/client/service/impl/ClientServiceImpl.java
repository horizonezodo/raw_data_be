package com.naas.admin_service.features.client.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.provider.IdentityStoreService;
import com.naas.admin_service.features.client.dto.ClientRequest;
import com.naas.admin_service.features.client.dto.RoleDto;
import com.naas.admin_service.features.client.service.ClientService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ClientServiceImpl implements ClientService {
    private final IdentityStoreService identityStoreService;

    @Override
    public List<ClientRepresentation> listClients() {
        ClientsResource clientsResource = getClientsResource();
        return clientsResource.findAll();
    }

    @Override
    public ClientRepresentation getClient(String id) {
        ClientResource clientResource = getClientResource(id);
        return clientResource.toRepresentation();
    }

    @Override
    public void createClient(ClientRequest request) {
        this.checkClientIdExist(request.getClientId());
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(request.getClientId());
        clientRepresentation.setName(request.getName());
        clientRepresentation.setDescription(request.getDescription());
        clientRepresentation.setPublicClient(request.getPublicClient());
        clientRepresentation.setAuthorizationServicesEnabled(request.getAuthorizationServicesEnabled());
        clientRepresentation.setStandardFlowEnabled(request.getStandardFlowEnabled());
        clientRepresentation.setDirectAccessGrantsEnabled(request.getDirectAccessGrantsEnabled());
        clientRepresentation.setImplicitFlowEnabled(request.getImplicitFlowEnabled());
        clientRepresentation.setServiceAccountsEnabled(request.getServiceAccountsEnabled());
        ClientsResource clientsResource = getClientsResource();
        Response response = clientsResource.create(clientRepresentation);
        if (response.getStatus() != 201) {
            throw new BusinessException(CommonErrorCode.ADD_CLIENT_FAILURE);
        }
    }

    @Override
    public void updateClient(String id, ClientRequest request) {
        ClientResource clientResource = getClientResource(id);
        ClientRepresentation clientRepresentation = clientResource.toRepresentation();
        clientRepresentation.setClientId(request.getClientId());
        clientRepresentation.setName(request.getName());
        clientRepresentation.setDescription(request.getDescription());
        clientRepresentation.setPublicClient(request.getPublicClient());
        clientRepresentation.setAuthorizationServicesEnabled(request.getAuthorizationServicesEnabled());
        clientRepresentation.setStandardFlowEnabled(request.getStandardFlowEnabled());
        clientRepresentation.setDirectAccessGrantsEnabled(request.getDirectAccessGrantsEnabled());
        clientRepresentation.setImplicitFlowEnabled(request.getImplicitFlowEnabled());
        clientRepresentation.setServiceAccountsEnabled(request.getServiceAccountsEnabled());
        clientResource.update(clientRepresentation);
    }

    @Override
    public void deleteClient(String id) {
        ClientResource clientResource = getClientResource(id);
        clientResource.remove();
    }

    @Override
    public List<RoleRepresentation> listClientRole(String id) {
        ClientResource clientResource = getClientResource(id);
        RolesResource rolesResource = clientResource.roles();
        return rolesResource.list();
    }

    @Override
    public List<RoleDto> listAllClientRoles() {
        List<RoleDto> allRoles = new ArrayList<>();
        List<ClientRepresentation> clients = getClientsResource().findAll();
        for (ClientRepresentation client : clients) {
            String id = client.getId();
            String clientId = client.getClientId();
            List<RoleRepresentation> clientRoles = listClientRole(id);
            for (RoleRepresentation role : clientRoles) {
                RoleDto roleDto = new RoleDto(
                        role.getId(),
                        role.getName(),
                        role.getDescription(),
                        role.isComposite(),
                        role.getClientRole(),
                        role.getContainerId(),
                        clientId);
                allRoles.add(roleDto);
            }
        }
        return allRoles;
    }

    @Override
    public RoleRepresentation getClientRole(String containerId, String roleName) {
        ClientResource clientResource = getClientResource(containerId);
        RolesResource rolesResource = clientResource.roles();
        return rolesResource.get(roleName).toRepresentation();
    }

    @Override
    public void createClientRole(String id, String roleName, String description) {
        ClientResource clientResource = getClientResource(id);
        RolesResource rolesResource = clientResource.roles();
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName(roleName);
        roleRepresentation.setDescription(description);
        rolesResource.create(roleRepresentation);
    }

    @Override
    public void updateClientRole(String id, String roleName, String description) {
        ClientResource clientResource = getClientResource(id);
        RolesResource rolesResource = clientResource.roles();
        RoleRepresentation roleRepresentation = rolesResource.get(roleName).toRepresentation();
        roleRepresentation.setDescription(description);
        rolesResource.get(roleName).update(roleRepresentation);
    }

    @Override
    public void deleteClientRole(String id, String roleName) {
        ClientResource clientResource = getClientResource(id);
        RolesResource rolesResource = clientResource.roles();
        rolesResource.get(roleName).remove();
    }

    @Override
    public List<UserRepresentation> listUserByClientRole(String clientId, String roleName) {
        ClientResource clientResource = getClientResource(clientId);
        RoleResource roleResource = clientResource.roles().get(roleName);

        List<UserRepresentation> result = new ArrayList<>();
        int first = 0;
        int max = 100;

        List<UserRepresentation> batch;
        do {
            batch = roleResource.getUserMembers(first, max);
            result.addAll(batch);
            first += max;
        } while (!batch.isEmpty());

        return result;
    }

    @Override
    public void assignClientRoleToUser(String clientId, String userId, String roleName) {
        ClientResource clientResource = getClientResource(clientId);
        RolesResource rolesResource = clientResource.roles();
        RoleRepresentation roleRepresentation = rolesResource.get(roleName).toRepresentation();

        UserResource userResource = identityStoreService.getUser(userId);
        userResource.roles()
                .clientLevel(clientId).add(Collections.singletonList(roleRepresentation));
    }

    public void assignServiceAccountRoles(String clientId, List<String> roleNames) {
        ClientResource clientResource = getClientResource(clientId);
        UserRepresentation serviceAccount = clientResource.getServiceAccountUser();
        List<RoleRepresentation> rolesToAssign = roleNames.stream()
                .map(roleName -> getRolesResource().get(roleName).toRepresentation())
                .toList();

        identityStoreService.getUser(serviceAccount.getId())
                .roles()
                .realmLevel()
                .add(rolesToAssign);
    }

    public void unAssignServiceAccountRoles(String clientId, List<String> roleNames) {
        ClientResource clientResource = getClientResource(clientId);
        UserRepresentation serviceAccount = clientResource.getServiceAccountUser();
        UserResource userResource = identityStoreService.getUser(serviceAccount.getId());

        List<RoleRepresentation> rolesToUnAssign = roleNames.stream()
                .map(roleName -> getRolesResource().get(roleName).toRepresentation())
                .toList();
        userResource.roles().realmLevel().remove(rolesToUnAssign);
    }

    public List<RoleRepresentation> getServiceAccountRoles(String clientId) {
        ClientResource clientResource = getClientResource(clientId);
        UserRepresentation serviceAccount = clientResource.getServiceAccountUser();
        return identityStoreService.getUser(serviceAccount.getId())
                .roles()
                .realmLevel()
                .listAll();
    }

    private void checkClientIdExist(String clientId) {
        List<ClientRepresentation> listClients = listClients();
        boolean clientIdExist = listClients.stream().anyMatch(client -> client.getClientId().equals(clientId));
        if (clientIdExist) {
            throw new BusinessException(CommonErrorCode.EXISTS, clientId);
        }
    }

    private ClientResource getClientResource(String id) {
        return identityStoreService.getClientResource(id);
    }

    private ClientsResource getClientsResource() {
        return identityStoreService.getClientsResource();
    }

    private RolesResource getRolesResource() {
        return identityStoreService.getRolesResource();
    }
}
