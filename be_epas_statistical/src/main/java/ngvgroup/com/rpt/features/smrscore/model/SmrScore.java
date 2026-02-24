package ngvgroup.com.rpt.features.smrscore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.util.Date;

@Entity
@Table(name = "SMR_SCORE")
@Getter
@Setter
public class SmrScore extends BaseEntity {
    @Column(name = "SCORE_INSTANCE_CODE", nullable = false, length = 128)
    private String scoreInstanceCode;

    @Column(name = "CI_ID", length = 128)
    private String ciId;

    @Column(name = "STAT_SCORE_TYPE_CODE", nullable = false, length = 128)
    private String statScoreTypeCode;

    @Column(name = "STAT_SCORE_TYPE_NAME", nullable = false, length = 256)
    private String statScoreTypeName;

    @Column(name = "WORKFLOW_CODE", nullable = false, length = 64)
    private String workflowCode;

    @Column(name = "VERSION_NO", nullable = false, precision = 7)
    private Integer versionNo;

    @Column(name = "TXN_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date txnDate;

    @Column(name = "SCORE_PERIOD", nullable = false, length = 32)
    private String scorePeriod;

    @Column(name = "SCORE_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date scoreDate;

    @Column(name = "MAKER_USER_CODE", nullable = false, length = 64)
    private String makerUserCode;

    @Column(name = "MAKER_USER_NAME", nullable = false, length = 128)
    private String makerUserName;

    @Column(name = "TXN_CONTENT", length = 4000)
    private String txnContent;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
