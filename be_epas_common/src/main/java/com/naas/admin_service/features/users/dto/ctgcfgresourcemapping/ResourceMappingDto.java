package com.naas.admin_service.features.users.dto.ctgcfgresourcemapping;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class ResourceMappingDto {
    private String resourceCode;
    private String resourceName;
    private String processGroupCode;
    private String processGroupName;
    private Date effectiveDate;
    private Date expiryDate;

    public ResourceMappingDto(String resourceCode, String resourceName, Date effectiveDate, Date expiryDate) {
        this.resourceCode = resourceCode;
        this.resourceName = resourceName;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
    }
}
