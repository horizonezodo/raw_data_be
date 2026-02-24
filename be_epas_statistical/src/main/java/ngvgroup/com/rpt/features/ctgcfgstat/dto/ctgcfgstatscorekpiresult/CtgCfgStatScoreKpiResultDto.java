package ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscorekpiresult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatScoreKpiResultDto {
    private Long id;
    private String resultName;
    private BigDecimal scoreValue;
    private BigDecimal scoreValueMax;
    private BigDecimal scoreValueMin;
    private String conditionExpression;
    private Long sortNumber;
    private String orgCode;
    private String recordStatus;
    private Integer isActive;
    private String kpiCode;


    public CtgCfgStatScoreKpiResultDto(Long id, String resultName, BigDecimal scoreValue, BigDecimal scoreValueMax, BigDecimal scoreValueMin, String conditionExpression, Long sortNumber) {
        this.id = id;
        this.resultName = resultName;
        this.scoreValue = scoreValue;
        this.scoreValueMax = scoreValueMax;
        this.scoreValueMin = scoreValueMin;
        this.conditionExpression = conditionExpression;
        this.sortNumber = sortNumber;
    }
}
