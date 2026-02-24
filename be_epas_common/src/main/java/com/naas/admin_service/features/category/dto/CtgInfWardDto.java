package com.naas.admin_service.features.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgInfWardDto {

    private String wardCode;
    private String wardName;
    private String provinceCode;
    private String provinceName;

    private String description;

    public CtgInfWardDto(String wardCode, String wardName, String provinceCode, String provinceName) {
        this.wardCode = wardCode;
        this.wardName = wardName;
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
    }





}
