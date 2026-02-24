package com.naas.admin_service.features.category.dto;

import lombok.Data;

@Data
public class CtgInfIndustryDto {
    private String industryCode;
    private String industryName;
    private String orgCode;
    private Integer isActive;
    private String description;

    public CtgInfIndustryDto(String industryCode, String industryName) {
        this.industryCode = industryCode;
        this.industryName = industryName;
    }
}
