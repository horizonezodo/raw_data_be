package com.naas.admin_service.features.users.dto.ctgcfgresourcemaster;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgResourceMasterDto {
    private Long id;
    private String resourceTypeCode;
    private String resourceTypeName;
    private String resourceSql;
}
