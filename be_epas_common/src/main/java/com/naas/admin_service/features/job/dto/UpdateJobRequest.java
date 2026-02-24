package com.naas.admin_service.features.job.dto;

import lombok.Getter;

@Getter
public class UpdateJobRequest {
    private String cronExpr;    // cron mới
    private String description; // mô tả mới (nếu muốn đổi)
}
