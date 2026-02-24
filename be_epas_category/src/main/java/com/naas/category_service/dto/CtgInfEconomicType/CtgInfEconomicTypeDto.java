package com.naas.category_service.dto.CtgInfEconomicType;

import lombok.Data;

@Data
public class CtgInfEconomicTypeDto {
    private String economicTypeCode;
    private String economicTypeName;
    private String orgCode;
    private Boolean isActive;
    private String description;

}
