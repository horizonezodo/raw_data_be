package com.naas.admin_service.features.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgInfProvinceDto {

    private String provinceCode;
    private String provinceName;
    private String taxCode;


    private String description;

    private String name;

    public CtgInfProvinceDto(String provinceCode, String provinceName, String taxCode) {
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.taxCode = taxCode;
    }

    public CtgInfProvinceDto(String provinceCode, String provinceName) {
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.setName(provinceCode+"-"+provinceName);
    }

    public CtgInfProvinceDto(String provinceCode, String provinceName, String taxCode, String description) {
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.taxCode = taxCode;
        this.description = description;
    }
}
