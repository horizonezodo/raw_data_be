package com.naas.admin_service.features.job.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateJobRequest {
    private String jobName;     // VD: CLEAR_LOG_ACTIVITY
    private String cronExpr;    // VD: "0 */1 * * * *"
    private String description; // mô tả
}
