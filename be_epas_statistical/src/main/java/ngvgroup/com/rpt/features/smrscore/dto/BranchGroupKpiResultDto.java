package ngvgroup.com.rpt.features.smrscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchGroupKpiResultDto {
    private Long id;
    private String kpiName;
    private String kpiValue;
    private String kpiCode;
    private BigDecimal achievedScore;
    private BigDecimal weightScore;
    private BigDecimal rawScore;
    private String statScoreGroupCode;

}
