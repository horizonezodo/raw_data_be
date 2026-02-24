package ngvgroup.com.loan.feature.interest_process.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class HistoryKey {
    private String decisionNo;
    private Date decisionDate;
    private Date effectiveDate;
}
