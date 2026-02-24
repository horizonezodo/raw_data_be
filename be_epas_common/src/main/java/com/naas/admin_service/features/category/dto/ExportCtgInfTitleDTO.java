package com.naas.admin_service.features.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportCtgInfTitleDTO {
    private String titleCode;
    private String titleName;
    private String positionName;
    private String orgName;
    private String status;
}
