package ngvgroup.com.bpm.features.transaction.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFilter {
    private String orgCode;
    private Timestamp fromDate;
    private Timestamp toDate;
    private List<String> processTypeCodes;
    private String slaResult;
    private Integer isAccounting;
    private String userId;
    private String userName;
    private String businessStatus;
}