package ngvgroup.com.fac.feature.sheet_import_export_process.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SheetInfoDto {
    @NotBlank(message = "Loại phiếu là bắt buộc")
    private String voucherTypeCode;
    @NotNull(message = "Số tiền là bắt buộc")
    @PositiveOrZero(message = "Số tiền không được âm")
    private BigDecimal totalForeignAmt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate txnDate;
    @NotBlank(message = "Nội dung giao dịch là bắt buộc")
    private String txnContent;
    private String orgCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime txnTime;
    @NotBlank(message = "Loại đối tượng là bắt buộc")
    private String objectTypeCode;
    private String objectTxnCode;
    @NotBlank(message = "Tên người giao dịch là bắt buộc")
    private String objectTxnName;
    @NotBlank(message = "Số CCCD là bắt buộc")
    private String identificationId;
    @NotNull(message = "Ngày cấp là bắt buộc")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;
    @NotBlank(message = "Nơi cấp là bắt buộc")
    private String issuePlace;
    @NotBlank(message = "Địa chỉ là bắt buộc")
    private String address;
    private String processInstanceCode;

    @NotEmpty(message = "Danh sách bút toán không được để trống")
    @Valid
    private List<AccountEntryDto> accounts;
    private List<AttachmentDto> attachments;
}
