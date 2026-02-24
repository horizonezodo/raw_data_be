package com.naas.admin_service.features.job.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntitySimple;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "COM_LOG_JOB")
public class ComLogJob extends BaseEntitySimple {

    @Column(name = "JOB_ID", nullable = false)
    private Long jobId;

    @Column(name = "JOB_NAME", length = 128)
    private String jobName;

    @Column(name = "STATUS", length = 32, nullable = false)
    private String status;        // RUNNING / SUCCESS / FAILED

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STARTED_AT")
    private Date startedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISHED_AT")
    private Date finishedAt;

    @Column(name = "RESULT_MESSAGE", length = 2000)
    private String resultMessage;
}

