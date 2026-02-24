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
public class StatScoreGroupRequest {
    private String statScoreGroupCode;
    private String statScoreGroupName;
    private BigDecimal weightScore;
    private Long sortNumber;
    private String statScoreTypeCode;
    private String statScoreGroupTypeCode;
    private String statScoreGroupTypeName;
    private String description;
}
