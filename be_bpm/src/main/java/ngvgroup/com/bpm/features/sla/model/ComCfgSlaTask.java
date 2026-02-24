package ngvgroup.com.bpm.features.sla.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COM_CFG_SLA_TASK")
public class ComCfgSlaTask extends BaseEntity {

    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    @Column(name = "TASK_DEFINE_CODE", length = 128, nullable = false)
    private String taskDefineCode;

    @Column(name = "PROCESS_DEFINE_CODE", length = 256)
    private String processDefineCode;

    @Column(name = "UNIT", length = 32)
    private String unit;

    @Column(name = "PRIORITY_LEVEL", length = 64)
    private String priorityLevel;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
