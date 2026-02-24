package ngvgroup.com.rpt.features.ctgcfgstatscorerank.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "CTG_CFG_STAT_SCORE_GROUP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgStatScoreGroup extends BaseEntity {
    @Column(name = "SORT_NUMBER")
    private Long sortNumber;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "STAT_SCORE_GROUP_CODE", nullable = false, length = 64)
    private String statScoreGroupCode;

    @Column(name = "STAT_SCORE_GROUP_NAME", nullable = false, length = 128)
    private String statScoreGroupName;

    @Column(name = "STAT_SCORE_TYPE_CODE", nullable = false, length = 128)
    private String statScoreTypeCode;

    @Column(name = "STAT_SCORE_GROUP_TYPE_CODE", length = 64)
    private String statScoreGroupTypeCode;

    @Column(name = "STAT_SCORE_GROUP_TYPE_NAME", length = 128)
    private String statScoreGroupTypeName;

    @Column(name = "WEIGHT_SCORE",precision = 7,scale = 4)
    private BigDecimal weightScore;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAT_SCORE_TYPE_CODE", referencedColumnName = "STAT_SCORE_TYPE_CODE", insertable = false, updatable = false)
    private CtgCfgStatScoreType statScoreType;
}
