package com.naas.admin_service.features.log.dto;

import java.util.Date;

import com.naas.admin_service.core.contants.ExcelColumns;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComInfLogAuditDto {
    @ExcelColumn(ExcelColumns.LOG_ID)
    private Long auditId;
    @ExcelColumn(ExcelColumns.LOG_TABLE_NAME)
    private String tableName;
    @ExcelColumn(ExcelColumns.LOG_FIELD_NAME)
    private String fieldName;
    @ExcelColumn(ExcelColumns.LOG_USERNAME)
    private String username;
    @ExcelColumn(ExcelColumns.LOG_EVENT_TIME)
    private Date eventTime;
    @ExcelColumn(ExcelColumns.LOG_CLIENT_IP)
    private String clientIp;
    @ExcelColumn(ExcelColumns.LOG_BROWSER_INFO)
    private String browserInfo;
    @ExcelColumn(ExcelColumns.LOG_OLD_VALUE)
    private String oldValue;
    @ExcelColumn(ExcelColumns.LOG_NEW_VALUE)
    private String newValue;
    private String requestId;

    public ComInfLogAuditDto(Long auditId, String fieldName, String oldValue, String newValue) {
        this.auditId = auditId;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
