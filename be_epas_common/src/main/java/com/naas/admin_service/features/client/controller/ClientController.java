package com.naas.admin_service.features.client.controller;

import com.naas.admin_service.features.client.dto.ClientRequest;
import com.naas.admin_service.features.client.dto.RoleDto;
import com.naas.admin_service.features.client.service.ClientService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PreAuthorize("hasRole('admin_client')")
    @GetMapping
    public ResponseEntity<ResponseData<List<ClientRepresentation>>> listClients() {
        List<ClientRepresentation> clients = clientService.listClients();
        return ResponseData.okEntity(clients);
    }

    @PreAuthorize("hasRole('admin_client')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<ClientRepresentation>> getClient(@PathVariable String id) {
        ClientRepresentation client = clientService.getClient(id);
        return ResponseData.okEntity(client);
    }

    @PreAuthorize("hasRole('admin_client')")
    @PostMapping
    public ResponseEntity<ResponseData<Void>> createClient(@Valid @RequestBody ClientRequest request) {
        clientService.createClient(request);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_client')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> updateClient(@PathVariable String id, @RequestBody ClientRequest request) {
        clientService.updateClient(id, request);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole('admin_client')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteClient(@PathVariable String id) {
        clientService.deleteClient(id);
        return ResponseData.noContentEntity();
    }

    @PreAuthorize("hasRole('admin_client')")
    @GetMapping("/list-role/{id}")
    public ResponseEntity<ResponseData<List<RoleRepresentation>>> listClientRoles(@PathVariable String id) {
        List<RoleRepresentation> clientRoles = clientService.listClientRole(id);
        return ResponseData.okEntity(clientRoles);
    }

    @PreAuthorize("hasRole('admin_client')")
    @GetMapping("/detail-role")
    public ResponseEntity<ResponseData<RoleRepresentation>> getClientRole(@RequestParam String containerId,
                                                                          @RequestParam String roleName) {
        RoleRepresentation clientRole = clientService.getClientRole(containerId, roleName);
        return ResponseData.okEntity(clientRole);
    }

    @PreAuthorize("hasRole('admin_client')")
    @PostMapping("/create-role")
    public ResponseEntity<ResponseData<Void>> createClientRole(@RequestParam String id,
                                                               @RequestParam String roleName,
                                                               @RequestParam(defaultValue = "") String description) {
        clientService.createClientRole(id, roleName, description);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_client')")
    @PutMapping("/update-role")
    public ResponseEntity<ResponseData<Void>> updateClientRole(@RequestParam String id,
                                                               @RequestParam String name,
                                                               @RequestParam(defaultValue = "") String description) {
        clientService.updateClientRole(id, name, description);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole('admin_client')")
    @DeleteMapping("/remove-role")
    public ResponseEntity<ResponseData<Void>> deleteClientRole(@RequestParam String id,
                                                               @RequestParam String roleName) {
        clientService.deleteClientRole(id, roleName);
        return ResponseData.noContentEntity();
    }

    @PreAuthorize("hasRole('admin_client')")
    @GetMapping("/list-user-by-client-role")
    public ResponseEntity<ResponseData<List<UserRepresentation>>> listUserByClientRole(@RequestParam String id, @RequestParam String roleName) {
        List<UserRepresentation> users = clientService.listUserByClientRole(id, roleName);
        return ResponseData.okEntity(users);
    }

    @PreAuthorize("hasRole('admin_client')")
    @GetMapping("/list-all-client-roles")
    public ResponseEntity<ResponseData<List<RoleDto>>> listAllClientRoles() {
        List<RoleDto> roles = clientService.listAllClientRoles();
        return ResponseData.okEntity(roles);
    }

    @PreAuthorize("hasRole('admin_client')")
    @PostMapping("/assign-service-account-roles")
    public ResponseEntity<ResponseData<Void>> assignServiceAccountRoles(@RequestParam String clientId, @RequestBody List<String> roleNames){
        clientService.assignServiceAccountRoles(clientId, roleNames);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_client')")
    @PostMapping("/un-assign-service-account-roles")
    public ResponseEntity<ResponseData<Void>> unAssignServiceAccountRoles(@RequestParam String clientId, @RequestBody List<String> roleNames){
        clientService.unAssignServiceAccountRoles(clientId, roleNames);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_client')")
    @GetMapping("/list-service-account-roles")
    public ResponseEntity<ResponseData<List<RoleRepresentation>>> getServiceAccountRoles(@RequestParam String clientId) {
        List<RoleRepresentation> roles = clientService.getServiceAccountRoles(clientId);
        return ResponseData.okEntity(roles);
    }
}
