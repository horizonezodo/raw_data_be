package com.naas.admin_service.features.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgInfIndustryResponse {
    private Long id;
    private String industryCode;
    private String industryName;
    private String orgCode;
    private String status;
    private Integer isActive;

}
