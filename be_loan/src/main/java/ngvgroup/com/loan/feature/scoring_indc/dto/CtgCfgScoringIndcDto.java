package ngvgroup.com.loan.feature.scoring_indc.dto;

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
public class CtgCfgScoringIndcDto {
    private String indicatorCode;
    private String indicatorName;
    private BigDecimal weightScore;
    private String controlType;
    private String dataType;
    private String sqlExecute;
    private String desc;
    private List<String> labels;
    private boolean isSelect;
    private int sortNumber;

    public CtgCfgScoringIndcDto(String indicatorCode, String indicatorName, BigDecimal weightScore, String controlType, String dataType) {
        this.indicatorCode = indicatorCode;
        this.indicatorName = indicatorName;
        this.weightScore = weightScore;
        this.controlType = controlType;
        this.dataType = dataType;
    }

    public CtgCfgScoringIndcDto(String indicatorCode, String indicatorName, boolean isSelect, int sortNumber) {
        this.indicatorCode = indicatorCode;
        this.indicatorName = indicatorName;
        this.isSelect = isSelect;
        this.sortNumber = sortNumber;
    }
}
