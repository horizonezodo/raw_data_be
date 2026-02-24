package com.naas.admin_service.features.job.dto;

import java.util.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobTriggerMessage {
    private Long logId;       // ID bản ghi COM_LOG_JOB
    private Long jobId;       // ID từ COM_CFG_JOB
    private String jobName;   // CLEAR_LOG_ACTIVITY,...
    private Date startedAt;   // thời điểm bắt đầu chạy
}