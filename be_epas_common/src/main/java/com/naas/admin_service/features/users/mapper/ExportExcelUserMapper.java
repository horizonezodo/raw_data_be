package com.naas.admin_service.features.users.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.naas.admin_service.features.users.dto.ExportExcelUserDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExportExcelUserMapper {
    @Mapping(source = "enabled", target = "enabled", qualifiedByName = "activeToStatus")
    ExportExcelUserDto toDto(UserRepresentation e);

    @Named("activeToStatus")
    default String mapActiveToStatus(boolean active) {
        return active ? "Đang hoạt động" : "Dừng hoạt động";
    }

    List<ExportExcelUserDto> toDtos(List<UserRepresentation> e);
}
