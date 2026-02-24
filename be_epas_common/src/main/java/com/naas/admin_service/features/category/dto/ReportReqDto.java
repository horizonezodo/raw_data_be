package com.naas.admin_service.features.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportReqDto {
    @NotBlank
    private String templateCode;

    private Object dataSource;

    private String format = "pdf";
}
