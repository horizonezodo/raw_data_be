package ngvgroup.com.loan.feature.interest_process.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FlatHistoryDetail {
    private InterestHistoryDTO parent;
    private InterestHistoryDetailDTO detail;
}