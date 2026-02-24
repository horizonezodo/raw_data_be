package ngvgroup.com.rpt.features.smrtxnscore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "SMR_TXN_SCORE_BRANCH_KPI")
@Getter
@Setter
public class SmrTxnScoreBranchKpi extends BaseEntity {

    @Column(name = "SCORE_INSTANCE_CODE", nullable = false, length = 128)
    private String scoreInstanceCode;

    @Column(name = "STAT_SCORE_GROUP_CODE", nullable = false, length = 64)
    private String statScoreGroupCode;

    @Column(name = "STAT_SCORE_GROUP_NAME", nullable = false, length = 128)
    private String statScoreGroupName;

    @Column(name = "KPI_CODE", nullable = false, length = 32)
    private String kpiCode;

    @Column(name = "KPI_NAME", nullable = false, length = 512)
    private String kpiName;

    @Column(name = "KPI_VALUE", nullable = false, length = 512)
    private String kpiValue;

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
