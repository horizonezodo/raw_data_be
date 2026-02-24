package ngvgroup.com.fac.feature.single_entry_acct.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TxnAcctDTOForm {

    private CommonTransactionInfoDTO common;

    @NotBlank(message = "Diễn giải không được để trống!")
    private String txnContent;

    @NotBlank(message = "Ngày nhập không được để trống!")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime txnTime;

    @NotBlank
    private BigDecimal totalForeignAmt;

    @NotBlank(message = "Loại đối tượng không được để trống!")
    @Size(max = 64, message = "Loại đối tượng không vượt quá 64 ký tự!")
    private String objectTypeCode;

    @NotBlank(message = "Mã người giao dịch không được để trống!")
    @Size(max = 64, message = "Mã người giao dịch không vượt quá 64 ký tự!")
    private String objectTxnCode;

    @NotBlank(message = "Tên người giao dịch không được để trống!")
    @Size(max = 128, message = "Tên người giao dịch không vượt quá 128 ký tự!")
    private String objectTxnName;

    @NotBlank(message = "Số CCCD không được để trống!")
    @Size(max = 128, message = "Số CCCD không vượt quá 128 ký tự!")
    private String identificationId;

    @NotBlank(message = "Ngày cấp không được để trống!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @NotBlank(message = "Nơi cấp không được để trống!")
    @Size(max = 256, message = "Nơi cấp không vượt quá 256 ký tự!")
    private String issuePlace;

    @NotBlank(message = "Địa chỉ không được để trống!")
    @Size(max = 512, message = "Địa chỉ không vượt quá 512 ký tự!")
    private String address;
}
