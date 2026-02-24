package com.naas.admin_service.features.setting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private String reportCode;
    private String reportGroupCode;
    private String reportCodeName;
    private String reportGroupName;
    private String dataSourceType;
    private String templateCode;
    private String reportSource;
    private BigDecimal sortNumber;
    private BigDecimal groupSortNumber;
    private boolean active;
}
