package com.naas.admin_service.features.job.dto;

import java.util.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobStatusMessage {
    private Long logId;
    private Long jobId;
    private String jobName;

    private String status;            // SUCCESS / FAILED
    private Date finishedAt;
    private String resultMessage;     // mô tả: "Deleted 1234 rows"
}
