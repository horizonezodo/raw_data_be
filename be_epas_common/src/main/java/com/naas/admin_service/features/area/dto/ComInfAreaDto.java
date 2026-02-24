package com.naas.admin_service.features.area.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComInfAreaDto {
    private String areaCode;
    private String areaName;


    private String wardCode;

    public ComInfAreaDto(String areaCode, String areaName) {
        this.areaCode = areaCode;
        this.areaName = areaName;
    }
}
