package com.naas.category_service.dto.CtgInfIndustry;

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
    private Boolean isActive;

}
