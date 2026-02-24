package ngvgroup.com.rpt.features.transactionreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregateReportResponseDto {
    private Long id;
    private String templateCode;
    private String statInstanceCode;
}
