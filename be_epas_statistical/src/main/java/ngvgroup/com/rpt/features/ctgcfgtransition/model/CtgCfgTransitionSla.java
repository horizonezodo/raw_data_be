package ngvgroup.com.rpt.features.ctgcfgtransition.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CTG_CFG_TRANSITION_SLA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgTransitionSla {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "DESCRIPTION", length = 512)
    private String description;

    @Column(name = "WORKFLOW_CODE", nullable = false, length = 64)
    private String workflowCode;

    @Column(name = "TRANSITION_CODE", nullable = false, length = 64)
    private String transitionCode;

    @Column(name = "DURATION_TIME", nullable = false)
    private Integer durationTime;

    @Column(name = "GRACE_TIME", nullable = false)
    private Integer graceTime;

    @Column(name = "USE_BUSINESS_TIME", nullable = false)
    private Integer useBusinessTime;

    @Column(name = "WARRNING_BEFORE_TIME")
    private Integer warrningBeforeTime;

    @Column(name = "ESCALATE_AFTER_TIME")
    private Integer escalateAfterTime;

    @Column(name = "WARRNING_SQL", length = 4000)
    private String warrningSql;

    @Column(name = "ESCALATE_SQL", length = 4000)
    private String escalateSql;

    @Column(name = "IS_ENABLE")
    private Boolean isEnable = true;

    @Column(name = "TIME_UNIT", length = 32)
    private String timeUnit;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
