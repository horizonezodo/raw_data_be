package ngvgroup.com.rpt.features.transactionreport.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RptTxnStatTemplateStatusDto {

    private Long id;

    @NotBlank
    @Size(max = 64)
    private String orgCode;

    @NotBlank
    @Size(max = 64)
    private String statInstanceCode;

    @NotBlank
    @Size(max = 128)
    private String templateCode;

    @Size(max = 4000)
    private String transitionComment;

    @NotNull
    private Timestamp transitionAt;

    @NotBlank
    @Size(max = 64)
    private String transitionCode;

    @NotBlank
    @Size(max = 128)
    private String transitionName;

    @NotBlank
    @Size(max = 64)
    private String statusCode;

    @NotBlank
    @Size(max = 128)
    private String statusName;

    @NotBlank
    @Size(max = 128)
    private String txnUserId;

    @NotBlank
    @Size(max = 256)
    private String txnUserName;

    @NotNull
    private Timestamp slaDueAt;

    private Timestamp slaActualAt;

    private Integer slaElapsedTime;

    @Size(max = 64)
    private String slaStatus;

    private Timestamp warningSentAt;

    private Timestamp escalatedAt;

    private Integer isEscalated;
}

