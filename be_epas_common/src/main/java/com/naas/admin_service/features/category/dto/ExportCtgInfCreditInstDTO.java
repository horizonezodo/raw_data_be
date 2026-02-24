package com.naas.admin_service.features.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportCtgInfCreditInstDTO {
    private String creditInstCode;
    private String creditInstName;
    private String address;
    private String status;
}
