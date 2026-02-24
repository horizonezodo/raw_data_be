package com.naas.admin_service.features.users.dto.ctgcfgresourcemaster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgResourceMasterDtoV2 {
    private String resourceCode;
    private String resourceName;
    private String resourceDesc;
    private String resourceTypeCode;
    private boolean check;
    private Date effectiveDate;
    private Date expiryDate;

    public CtgCfgResourceMasterDtoV2(String resourceCode, String resourceDesc, String resourceName, boolean check) {
        this.resourceCode = resourceCode;
        this.resourceDesc = resourceDesc;
        this.resourceName = resourceName;
        this.check = check;
    }
}
