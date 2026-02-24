package ngvgroup.com.rpt.features.ctgcfgstatscorerank.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_CFG_STAT_SCORE_BENCHMARK")
public class CtgCfgStatScoreBenchmark extends BaseEntity {
    

    @Column(name = "BENCHMARK_CODE", length = 128)
    private String benchmarkCode;

    @Column(name = "BENCHMARK_NAME", length = 256)
    private String benchmarkName;

    @Column(name = "STAT_SCORE_TYPE_CODE", length = 128)
    private String statScoreTypeCode;

    @Column(name = "BENCHMARK_VALUE", length = 128)
    private String benchmarkValue;

    @Column(name = "BENCHMARK_DESC", length = 4000)
    private String benchmarkDesc;

    @Column(name = "SCORE_VALUE_MIN", precision = 7, scale = 4)
    private BigDecimal scoreValueMin;

    @Column(name = "SCORE_VALUE_MAX", precision = 7, scale = 4)
    private BigDecimal scoreValueMax;

    @Column(name = "CONDITION_EXPRESSION", length = 256)
    private String conditionExpression;

    @Column(name = "SORT_NUMBER")
    private Long sortNumber;

    @Column(name = "ORG_CODE")
    private String orgCode;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAT_SCORE_TYPE_CODE", referencedColumnName = "STAT_SCORE_TYPE_CODE", insertable = false, updatable = false)
    private CtgCfgStatScoreType statScoreType;

}

