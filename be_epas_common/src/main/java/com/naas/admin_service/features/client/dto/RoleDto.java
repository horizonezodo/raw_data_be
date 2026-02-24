package com.naas.admin_service.features.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private String id;
    private String name;
    private String description;
    private Boolean composite;
    private Boolean clientRole;
    private String containerId;
    private String clientId;
}
