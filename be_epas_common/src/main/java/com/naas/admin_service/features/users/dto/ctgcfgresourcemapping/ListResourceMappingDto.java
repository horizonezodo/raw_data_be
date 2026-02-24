package com.naas.admin_service.features.users.dto.ctgcfgresourcemapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListResourceMappingDto {
    private Long id;
    private String resourceCode;
    private String resourceName;
    private Date effectiveDate;
    private Date expiryDate;
}
