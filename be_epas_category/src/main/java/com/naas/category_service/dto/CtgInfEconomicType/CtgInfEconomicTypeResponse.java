package com.naas.category_service.dto.CtgInfEconomicType;

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
    private Boolean isActive;
    private String orgCode;

}
