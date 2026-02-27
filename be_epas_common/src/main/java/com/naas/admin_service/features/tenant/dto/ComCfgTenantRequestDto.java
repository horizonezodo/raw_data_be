package com.naas.admin_service.features.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComCfgTenantRequestDto {
    private Long id;
    private String tenantId;
    private String tenantName;
    private String dbType;
    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClass;
    private String active;
}