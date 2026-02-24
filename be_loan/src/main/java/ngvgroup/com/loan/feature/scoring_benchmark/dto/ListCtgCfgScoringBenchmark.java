package ngvgroup.com.loan.feature.scoring_benchmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListCtgCfgScoringBenchmark {
    private String benchMarkCode;
    private String benchMarkName;
    private String scoringIndcGroupType;
    private String benchMarkValue;
    private BigDecimal scoreValueMin;
    private BigDecimal scoreValueMax;
    private String desc;
}
