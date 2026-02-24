package ngvgroup.com.loan.feature.scoring_benchmark.dto;

import java.math.BigDecimal;

public interface ScoringBenchmarkProjection {
    String getBenchmarkCode();
    String getBenchmarkName();
    String getIndicatorName();
    String getBenchmarkValue();
    BigDecimal getScoreValueMin();
    BigDecimal getScoreValueMax();
    String getDescription();
}
