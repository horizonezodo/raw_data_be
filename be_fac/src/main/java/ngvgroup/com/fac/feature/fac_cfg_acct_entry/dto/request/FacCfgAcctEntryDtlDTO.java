package ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class FacCfgAcctEntryDtlDTO {
    private Long id;

    @NotBlank(message = "Loại bút toán không được để trống!")
    @Size(max = 64, message = "Loại bút toán không vượt quá 64 ký tự!")
    private String entryType;

    @NotBlank(message = "Nguồn tài khoản không được để trống!")
    @Size(max = 128, message = "Nguồn tài khoản không vượt quá 128 ký tự!")
    private String accSideType;

    @Size(max = 128, message = "Mã phân loại tài khoản không vượt quá 128 ký tự!")
    private String accClassCode;
    private String accClassName;

    @NotBlank(message = "Loại số tiền không được để trống!")
    @Size(max = 128, message = "Loại số tiền không vượt quá 128 ký tự!")
    private String amtType;

    @NotBlank(message = "Tham số tiền không được để trống!")
    @Size(max = 128, message = "Tham số tiền không vượt quá 128 ký tự!")
    private String amtParamCode;

    @NotNull(message = "Thứ tự phát sinh chi tiết không được để trống!")
    private Integer entrySeqNo;

    private String description;
    private String entryCode;
    private String orgCode;
}
