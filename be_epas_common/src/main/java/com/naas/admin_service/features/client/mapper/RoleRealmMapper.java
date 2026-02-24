package com.naas.admin_service.features.client.mapper;

import org.keycloak.representations.idm.RoleRepresentation;
import org.mapstruct.Mapper;

import com.naas.admin_service.features.client.dto.RoleDto;

@Mapper(componentModel = "spring")
public interface RoleRealmMapper {
    RoleDto toDto(RoleRepresentation roleRepresentations);
    RoleRepresentation toEntity(RoleDto roleDto);
}
