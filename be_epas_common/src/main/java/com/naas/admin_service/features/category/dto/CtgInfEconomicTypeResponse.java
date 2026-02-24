package com.naas.admin_service.features.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgInfEconomicTypeResponse {
    private Long id;
    private String economicTypeCode;
    private String economicTypeName;
    private String orgName;
    private String status;
    private Integer isActive;
    private String orgCode;

    public CtgInfEconomicTypeResponse(String economicTypeCode, String economicTypeName) {
        this.economicTypeCode = economicTypeCode;
        this.economicTypeName = economicTypeName;
    }
}
