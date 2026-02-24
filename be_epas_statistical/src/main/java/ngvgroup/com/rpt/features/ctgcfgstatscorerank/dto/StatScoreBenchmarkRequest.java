package ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatScoreBenchmarkRequest {
    private String benchmarkCode;

    private String benchmarkName;

    private String statScoreTypeCode;

    private String benchmarkValue;

    private BigDecimal scoreValueMax;

    private BigDecimal scoreValueMin;

    private String benchmarkDesc;

    private String conditionExpression;


}
