package ngvgroup.com.fac.feature.sheet_import_export_process.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntryDto {
    private String voucherNo;
    private String voucherTypeName;
    private String voucherGroup;
    @NotBlank(message = "Mã phân loại là bắt buộc")
    private String accClassCode;
    @NotBlank(message = "Số tài khoản là bắt buộc")
    private String accNo;
    private String entryType;
    private BigDecimal lineForeignAmt;
    private BigDecimal balAvailable;
    private BigDecimal balActual;
}
