package com.naas.admin_service.features.log.dto;

import java.util.Date;

import com.naas.admin_service.core.contants.ExcelColumns;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComInfLogActivityDto {
    @ExcelColumn(ExcelColumns.LOG_ID)
    private Long activityId;
    @ExcelColumn(ExcelColumns.LOG_EVENT_TIME)
    private Date eventTime;
    @ExcelColumn(ExcelColumns.LOG_USERNAME)
    private String username;
    @ExcelColumn(ExcelColumns.LOG_FUNCTION_CODE)
    private String functionCode;
    @ExcelColumn(ExcelColumns.LOG_ACTION_NAME)
    private String actionName;
    @ExcelColumn(ExcelColumns.LOG_DURATION_TIME)
    private Long durationTime;
    @ExcelColumn(ExcelColumns.LOG_CLIENT_IP)
    private String clientIp;
    @ExcelColumn(ExcelColumns.LOG_BROWSER_INFO)
    private String browserInfo;
    @ExcelColumn(ExcelColumns.LOG_STATUS_CODE)
    private String statusCode;
    private String requestPayload;
}