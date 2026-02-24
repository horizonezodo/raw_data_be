package ngvgroup.com.rpt.features.transactionreport.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class StatusCreationParamsDto {
    String orgCode;
    String statInstanceCode;
    String templateCode;
    String transitionComment;
    String transitionCode;
    String transitionName;
    String statusCode;
    String statusName;
    Timestamp slaDueAt;
    Integer warningTimeInMinutes;
}
