package ngvgroup.com.rpt.features.smrtxnscore.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ScoreRequestDto {
    private String ciId;
    private String statScoreTypeCode;
    private String statScoreTypeName;
    private String scorePeriod;
    private String makerUserName;
    private String scoreInstanceCode;
    private Date scoreDate;
    private Date txnDate;
    private Integer year;
    private Integer month;
    private Integer day;
    private String precious;
}