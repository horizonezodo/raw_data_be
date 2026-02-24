package com.naas.admin_service.features.users.service;

import com.naas.admin_service.features.users.dto.RoleResponseDto;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface RoleService {


    List<RoleResponseDto> listRoles();
    RoleResponseDto getRole(String roleName) ;

    void createRole(String roleName, String description) ;
    void updateRole(String roleName, String description) ;
    void deleteRole(String roleName) ;

    List<UserRepresentation> listUserByRole(String roleName) ;

}
