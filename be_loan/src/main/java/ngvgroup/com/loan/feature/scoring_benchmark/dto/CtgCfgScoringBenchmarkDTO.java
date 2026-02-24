package ngvgroup.com.loan.feature.scoring_benchmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgScoringBenchmarkDTO {
    private int sortNumber;
    private String benchmarkCode;
    private String benchmarkName;
    private String scoringIndcGroupType;
    private String scoringTypeCode;
    private String benchmarkValue;
    private String benchmarkDesc;
    private BigDecimal scoreValueMin;
    private BigDecimal scoreValueMax;
    private String conditionExpression;
    private String desc;
    private List<String> labels;
}
