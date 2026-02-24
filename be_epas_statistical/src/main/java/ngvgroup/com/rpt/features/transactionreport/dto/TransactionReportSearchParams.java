package ngvgroup.com.rpt.features.transactionreport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReportSearchParams {

    private String orgCode;
    private String regulatoryTypeCode;
    private String reportPeriod;
    private List<String> statusCodes;
    private String statInstanceCode;
    private String templateCode;
    private Date fromDate;
    private Date toDate;
    private List<String> templateGroupCodes;
    private List<String> circularCodes;
    private String keyword;
    private List<String> defaultCircularCodes;
}
