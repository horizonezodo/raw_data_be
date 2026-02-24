package ngvgroup.com.rpt.features.transactionreport.dto.sub;

import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdjustmentInformationDto {
    private Long id;
    private String kpiName;
    private String kpiOldValue;
    private String kpiNewValue;
    private Timestamp changedAt;
}
