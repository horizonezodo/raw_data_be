package ngvgroup.com.rpt.features.smrscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchGroupInfo {
    private Long id;
    private BigDecimal achievedScore;
    private BigDecimal weightScore;
    private BigDecimal rawScore;
    private String statScoreGroupName;
    private String statScoreGroupCode;
    private String scoreInstantCode;
    private String ciBrId;
    private String ciId;
}
