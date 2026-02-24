package ngvgroup.com.bpm.features.sla.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COM_CFG_SLA_TASK_DTL")
public class ComCfgSlaTaskDtl extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    @Column(name = "TASK_DEFINE_CODE", length = 128, nullable = false)
    private String taskDefineCode;

    @Column(name = "PROCESS_DEFINE_CODE", length = 256, nullable = false)
    private String processDefineCode;

    @Column(name = "EFFECTIVE_DATE", nullable = false)
    private Date effectiveDate;

    @Column(name = "SLA_MAX_DURATION")
    private Double slaMaxDuration;

    @Column(name = "SLA_WARNING_DURATION")
    private Double slaWarningDuration;

    @Column(name = "SLA_WARNING_TYPE", length = 64)
    private String slaWarningType;

    @Column(name = "SLA_WARNING_PERCENT")
    private Double slaWarningPercent;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}

