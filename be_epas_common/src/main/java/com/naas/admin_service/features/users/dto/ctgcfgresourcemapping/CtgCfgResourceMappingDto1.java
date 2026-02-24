package com.naas.admin_service.features.users.dto.ctgcfgresourcemapping;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CtgCfgResourceMappingDto1 {
    private String resourceCode;
    private String resourceName;
    private String orgCode;
    private String areaCode;
    private Date effectiveDate;
    private Date expiryDate;

    public CtgCfgResourceMappingDto1(String resourceCode, String resourceName, Date effectiveDate, Date expiryDate) {
        this.resourceCode = resourceCode;
        this.resourceName = resourceName;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
    }

    public CtgCfgResourceMappingDto1(String resourceCode, String resourceName, String orgCode, String areaCode, Date effectiveDate, Date expiryDate) {
        this.resourceCode = resourceCode;
        this.resourceName = resourceName;
        this.orgCode = orgCode;
        this.areaCode = areaCode;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
    }
}
