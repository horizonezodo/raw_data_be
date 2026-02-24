package ngvgroup.com.rpt.features.smrtxnscore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "SMR_TXN_SCORE_BRANCH")
@Getter
@Setter
public class SmrTxnScoreBranch extends BaseEntity {
    @Column(name = "SCORE_INSTANCE_CODE", nullable = false, length = 128)
    private String scoreInstanceCode;

    @Column(name = "CI_ID", nullable = false, length = 128)
    private String ciId;

    @Column(name = "CI_BR_ID", nullable = false, length = 128)
    private String ciBrId;

    @Column(name = "STAT_INSTANCE_CODE", nullable = false, length = 64)
    private String statInstanceCode;

    @Column(name = "TXN_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date txnDate;

    @Column(name = "SCORE_PERIOD", nullable = false, length = 32)
    private String scorePeriod;

    @Column(name = "SCORE_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date scoreDate;

    @Column(name = "ACHIEVED_SCORE", nullable = false, precision = 11, scale = 4)
    private BigDecimal achievedScore;

    @Column(name = "RANK_VALUE", nullable = false, length = 128)
    private String rankValue;

    @Column(name = "RANK_CONTENT", nullable = false, length = 256)
    private String rankContent;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}