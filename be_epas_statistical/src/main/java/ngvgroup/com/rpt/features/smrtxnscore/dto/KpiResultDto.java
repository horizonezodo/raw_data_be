package ngvgroup.com.rpt.features.smrtxnscore.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class KpiResultDto {
    private String statScoreGroupCode;
    private String statScoreGroupName;
    private String kpiCode;
    private String kpiName;
    private BigDecimal kpiValue;
    private BigDecimal achievedScore; // điểm đạt được (theo bảng result)
    private BigDecimal weightScore; // trọng số kpi (cấu hình)
    private BigDecimal rawScore; // achieved * weight
}
