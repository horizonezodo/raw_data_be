package com.naas.admin_service.features.category.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgInfTitleDTO {
    private Long id;
    private String orgCode;
    private String titleCode;
    private String titleName;
    private String positionCode;
    private String positionName;
    private String investorRole;
    private String educaionLevel;
    private String functionTask;
    private int isDelete;
    private String description;
    private String status;
}
