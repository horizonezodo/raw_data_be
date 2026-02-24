package ngvgroup.com.rpt.features.transactionreport.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RptTxnStatTemplateDto {

    private Long id;

    @NotBlank
    @Size(max = 64)
    private String orgCode;

    @NotBlank
    @Size(max = 64)
    private String statInstanceCode;

    @Size(max = 64)
    private String prevStatInstanceCode;

    @NotNull
    private Integer aggregationRunNo;

    @NotNull
    private Integer revNo;

    @NotNull
    private LocalDate txnDate;

    @NotBlank
    @Size(max = 64)
    private String makerUserCode;

    @NotBlank
    @Size(max = 128)
    private String makerUserName;

    @NotBlank
    @Size(max = 64)
    private String workflowCode;

    @NotBlank
    @Size(max = 128)
    private String templateCode;

    @NotBlank
    @Size(max = 256)
    private String templateName;

    @NotBlank
    @Size(max = 32)
    private String reportPeriod;

    @NotNull
    private LocalDate reportDataDate;

    @NotBlank
    @Size(max = 64)
    private String currentStatusCode;

    @NotBlank
    @Size(max = 128)
    private String currentStatusName;

    @NotBlank
    @Size(max = 64)
    private String lastTransitionCode;

    @NotNull
    private Timestamp startAt;

    @NotNull
    private Timestamp slaDueAt;

    @NotNull
    private Integer slaElapsedTime;

    @NotNull
    private Integer isVoid;

    @Size(max = 512)
    private String voidReason;

    @NotBlank
    @Size(max = 32)
    private String slaResult;

    @NotNull
    private Timestamp reportDueTime;

    private Timestamp warningSentAt;

    private Timestamp escalatedAt;

    private Integer isEscalated;

    private Integer sendCount;

    private Integer exportCount;
}