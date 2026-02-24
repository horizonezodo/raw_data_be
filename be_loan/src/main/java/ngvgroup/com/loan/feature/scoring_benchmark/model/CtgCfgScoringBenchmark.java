package ngvgroup.com.loan.feature.scoring_benchmark.model;

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
@Table(name = "CTG_CFG_SCORING_BENCHMARK")
public class CtgCfgScoringBenchmark extends BaseEntity {

    @Column(name = "SORT_NUMBER", length = 22 ,nullable = false)
    private int sortNumber;
    @Column(name = "BENCHMARK_CODE", length = 128 ,nullable = false)
    private String benchmarkCode;
    @Column(name = "BENCHMARK_NAME", length = 256 ,nullable = false)
    private String benchmarkName;
    @Column(name = "SCORING_INDC_GROUP_TYPE", length = 128 ,nullable = false)
    private String scoringIndcGroupType;
    @Column(name = "SCORING_TYPE_CODE", length = 128 ,nullable = false)
    private String scoringTypeCode;
    @Column(name = "BENCHMARK_VALUE", length = 128 ,nullable = false)
    private String benchmarkValue;
    @Column(name = "BENCHMARK_DESC", length = 4000 ,nullable = false)
    private String benchmarkDesc;
    @Column(name = "SCORE_VALUE_MIN", length = 7,scale = 4 ,nullable = false)
    private BigDecimal scoreValueMin;
    @Column(name = "SCORE_VALUE_MAX", length = 7,scale = 4)
    private BigDecimal scoreValueMax;
    @Column(name = "CONDITION_EXPRESSION", length = 256)
    private String conditionExpression;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
