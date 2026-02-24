package com.naas.admin_service.features.area.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComInfAreaRequestDto {
    private String areaCode;
    private String orgCode;
    private String areaName;
    private String areaTypeCode;
    private String wardCode;
    private String areaType;
    private Boolean isRuralArea;
    private String description;
    private String filter;
    private List<String> areaTypeCodes;
    private List<String> labels;
}
