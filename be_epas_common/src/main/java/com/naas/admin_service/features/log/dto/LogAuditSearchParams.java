package com.naas.admin_service.features.log.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogAuditSearchParams {
    private Date fromDate;
    private Date toDate;
    private String browserInfo;
    private String username;
    private String clientIp;
    private String tableName;
    private String keyword;
}