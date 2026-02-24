package ngvgroup.com.loan.feature.interest_process.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InterestHistoryDTO {

    private List<InterestHistoryDetailDTO> historyDetails;

    private Date effectiveDate;

    private Date decisionDate;

    private String decisionNo;
}
