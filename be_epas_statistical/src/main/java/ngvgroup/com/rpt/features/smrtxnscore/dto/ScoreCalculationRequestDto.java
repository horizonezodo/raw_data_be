package ngvgroup.com.rpt.features.smrtxnscore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreCalculationRequestDto {
    private String ciId; // đơn vị chấm điểm
    private String statScoreTypeCode; // loại chấm điểm
    private String scoreDate; // ngày chấm điểm
}