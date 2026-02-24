package com.ngvgroup.bpm.core.logging.audit.dto;

import com.ngvgroup.bpm.core.logging.audit.domain.ChangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor        // 👈 bắt buộc cho Jackson
@AllArgsConstructor
public class AuditLogMessage {

    private String requestId;

    // Bảng & bản ghi
    private String tableName;
    private String entityName;
    private String recordId;
    private ChangeType changeType;

    // Thông tin user / request
    private String username;
    private String clientIp;
    private String browserInfo;
    private Date eventTime;

    // Danh sách field thay đổi
    private List<AuditFieldChange> changes;
}