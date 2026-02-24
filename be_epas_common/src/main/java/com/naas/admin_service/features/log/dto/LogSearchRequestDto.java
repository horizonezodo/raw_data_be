package com.naas.admin_service.features.log.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogSearchRequestDto {
    private Date fromDate;
    private Date toDate;
    private String browserInfo;
    private String username;
    private String clientIp;
    private String tableName;
}
