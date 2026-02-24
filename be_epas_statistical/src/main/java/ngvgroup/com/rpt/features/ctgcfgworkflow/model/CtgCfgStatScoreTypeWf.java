package ngvgroup.com.rpt.features.ctgcfgworkflow.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "CTG_CFG_STAT_SCORE_TYPE_WF")
@Getter
@Setter
public class CtgCfgStatScoreTypeWf extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    @Column(name = "STAT_SCORE_TYPE_CODE", length = 128, nullable = false)
    private String statScoreTypeCode;

    @Column(name = "WORKFLOW_CODE", length = 64, nullable = false)
    private String workflowCode;

    @Column(name = "VERSION_NO")
    private Integer versionNo;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
