package com.naas.admin_service.features.job.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComLogJobDto {
    private Long id;
    private Long jobId;
    private String jobName;
    private String status;
    private Date startedAt;
    private Date finishedAt;
    private String resultMessage;
}
