package com.naas.admin_service.features.category.dto;

import lombok.Data;

@Data
public class CtgInfEconomicTypeDto {
    private String economicTypeCode;
    private String economicTypeName;
    private String orgCode;
    private Integer isActive;
    private String description;

}
