package com.naas.admin_service.features.job.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgJobDto {
    private Long id;
    private String jobName;
    private String cronExpr;
    private Integer isActive;
    private String description;
}
