package ngvgroup.com.rpt.features.transactionreport.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RptTxnStatTemplateCheckDto {

    private Long id;

    @NotBlank
    @Size(max = 64)
    private String statInstanceCode;

    @NotBlank
    @Size(max = 64)
    private String ruleCode;

    @NotBlank
    @Size(max = 256)
    private String ruleName;

    @Size(max = 128)
    private String templateCode;

    @Size(max = 32)
    private String kpiCode;

    @NotBlank
    @Size(max = 64)
    private String ruleMode;

    @NotBlank
    @Size(max = 64)
    private String responseCode;

    @Size(max = 4000)
    private String errorMsg;

    private Timestamp detectedAt;

    @Size(max = 64)
    private String detectedBy;
}
