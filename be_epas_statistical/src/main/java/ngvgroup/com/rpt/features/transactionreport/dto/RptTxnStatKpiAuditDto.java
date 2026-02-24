package ngvgroup.com.rpt.features.transactionreport.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RptTxnStatKpiAuditDto {

    private Long id;

    @NotBlank
    @Size(max = 64)
    private String statInstanceCode;

    @NotBlank
    @Size(max = 32)
    private String kpiCode;

    @NotBlank
    @Size(max = 4000)
    private String kpiOldValue;

    @Size(max = 4000)
    private String kpiNewValue;

    @Size(max = 4000)
    private String editReason;

    @NotBlank
    @Size(max = 128)
    private String txnUserId;

    @NotBlank
    @Size(max = 256)
    private String txnUserName;

    private Timestamp changedAt;
}
