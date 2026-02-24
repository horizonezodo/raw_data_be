package ngvgroup.com.rpt.features.transactionreport.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RptTxnStatTemplateKpiDto {

    private Long id;

    @NotBlank
    @Size(max = 64)
    private String orgCode;

    @NotBlank
    @Size(max = 64)
    private String statInstanceCode;

    @NotNull
    private Integer aggregationRunNo;

    @NotNull
    private Integer revNo;

    @NotBlank
    @Size(max = 32)
    private String kpiCode;

    @NotBlank
    @Size(max = 512)
    private String kpiName;

    @NotNull
    private LocalDate reportDataDate;

    private Long currencyCode;

    @Size(max = 4000)
    private String kpiValue;
}
