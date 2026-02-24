package ngvgroup.com.rpt.features.smrscore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "SMR_SCORE_BRANCH_GROUP")
@Getter
@Setter
public class SmrScoreBranchGroup extends BaseEntity {
    @Column(name = "SCORE_INSTANCE_CODE", nullable = false, length = 128)
    private String scoreInstanceCode;

    @Column(name = "STAT_SCORE_GROUP_CODE", nullable = false, length = 64)
    private String statScoreGroupCode;

    @Column(name = "STAT_SCORE_GROUP_NAME", nullable = false, length = 128)
    private String statScoreGroupName;

    @Column(name = "TXN_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date txnDate;

    @Column(name = "SCORE_PERIOD", nullable = false, length = 32)
    private String scorePeriod;

    @Column(name = "SCORE_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date scoreDate;

    @Column(name = "RAW_SCORE", nullable = false, precision = 11, scale = 4)
    private BigDecimal rawScore;

    @Column(name = "WEIGHT_SCORE", nullable = false, precision = 11, scale = 4)
    private BigDecimal weightScore;

    @Column(name = "ACHIEVED_SCORE", nullable = false, precision = 11, scale = 4)
    private BigDecimal achievedScore;

    @Column(name = "CI_ID", nullable = false, length = 128)
    private String ciId;

    @Column(name = "CI_BR_ID", nullable = false, length = 128)
    private String ciBrId;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}