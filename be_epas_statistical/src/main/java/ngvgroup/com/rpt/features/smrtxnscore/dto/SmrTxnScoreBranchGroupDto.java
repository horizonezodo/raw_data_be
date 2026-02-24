package ngvgroup.com.rpt.features.smrtxnscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmrTxnScoreBranchGroupDto {

    private Long id;
    private String recordStatus;
    private Integer isActive;
    private String scoreInstanceCode;
    private String statScoreGroupCode;
    private String statScoreGroupName;
    private Date txnDate;
    private String scorePeriod;
    private Date scoreDate;
    private BigDecimal rawScore;
    private BigDecimal weightScore;
    private BigDecimal achievedScore;
}
