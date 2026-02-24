package ngvgroup.com.rpt.features.ctgcfgstat.dto.ctgcfgstatscoregroupkpi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgStatScoreGroupKpiDto {
    private String statScoreGroupCode;
    private String kpiCode;
    private BigDecimal weightScore;
    private Long sortNumber;
}
