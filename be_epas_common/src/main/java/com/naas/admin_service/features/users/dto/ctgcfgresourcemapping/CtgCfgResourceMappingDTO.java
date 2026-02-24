package com.naas.admin_service.features.users.dto.ctgcfgresourcemapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgResourceMappingDTO {
    private Long id;
    private String description;
    private String resourceTypeCode;
    private String userId;
    private String groupId;
    private String resourceCode;
    private String resourceName;
    private String resourceDesc;
}
