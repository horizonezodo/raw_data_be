package com.naas.admin_service.features.job.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntitySimple;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "COM_CFG_JOB")
public class ComCfgJob  extends BaseEntitySimple {

    @Column(name = "JOB_NAME", nullable = false, length = 128, unique = true)
    private String jobName;

    @Column(name = "CRON_EXPR", nullable = false, length = 64)
    private String cronExpr;      // ví dụ: "*/30 * * * * *"

    @Column(name = "IS_ACTIVE")
    private Integer isActive;     // 1: chạy, 0: tắt

    @Column(name = "DESCRIPTION", length = 512)
    private String description;
}
