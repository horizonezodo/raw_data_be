package ngvgroup.com.fac.feature.single_entry_acct.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountingInfoDTO {
    private Long idDtl;

    @NotBlank(message = "Mã phân loại không được để trống!")
    private String accClassCode;

    @NotBlank(message = "Ghi nhận không được để trống!")
    private String entryType;

    @NotBlank(message = "Số tài khoản không được để trống!")
    private String accNo;

    @NotBlank(message = "Số tiền phát sinh không được để trống!")
    private BigDecimal lineForeignAmt;

    @NotBlank
    private String orgCode;

    @NotBlank
    private String accCoaCode;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate txnDate;

    private BigDecimal lineBaseAmt;
    private BigDecimal balAvailable;
    private BigDecimal balActual;
}
