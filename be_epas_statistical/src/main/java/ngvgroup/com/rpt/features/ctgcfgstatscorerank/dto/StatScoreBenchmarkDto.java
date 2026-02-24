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
public class StatScoreBenchmarkDto {
    private Long id;

    private String benchmarkCode;

    private String benchmarkName;

    private String statScoreTypeCode;

    private String statScoreTypeName;

    private String benchmarkValue;

    private String benchmarkDesc;

    private BigDecimal scoreValueMin;

    private BigDecimal scoreValueMax;

    private String conditionExpression;
}
