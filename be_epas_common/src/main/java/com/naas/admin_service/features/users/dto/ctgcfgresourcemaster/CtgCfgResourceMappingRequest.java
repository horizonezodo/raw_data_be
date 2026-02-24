package com.naas.admin_service.features.users.dto.ctgcfgresourcemaster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgResourceMappingRequest {
    private String resourceTypeCode;
    private List<CtgCfgResourceMasterDtoV2> lstResourceMapping;
}
