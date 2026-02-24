package ngvgroup.com.loan.feature.scoring_indc_result.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgScoringIndcResultDTO {
    private String resultName;
    private BigDecimal scoreValue;
    private BigDecimal scoreValueMax;
    private BigDecimal scoreValueMin;
    private String conditionExpression;
    private int sortNumber;
}
