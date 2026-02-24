package ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacTxnAcctDTO {

    @NotBlank
    private LocalDate txnDate;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime txnTime;
    @NotBlank
    private String txnContent;

    @NotBlank
    private String orgCode;

    private String objectTypeCode;

    private String objectTxnCode;

    private String objectTxnName;

    private String identificationId;

    private LocalDate issueDate;

    private String issuePlace;

    private String address;

    @NotBlank
    private String processInstanceCode;

    @NotBlank
    private BigDecimal totalForeignAmt;

    @NotBlank
    private BigDecimal totalBaseAmt;

    private BigDecimal totalTaxAmt;

    private BigDecimal totalFeeAmt;

    private BigDecimal totalAdjustAmt;

    private BigDecimal totalReversedAmt;

    @NotBlank
    private String businessStatus;
    @NotBlank
    private String currencyCode;
    @NotBlank
    private String processTypeCode;
}
