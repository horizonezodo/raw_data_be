package ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FacCfgAcctEntryDTO {
    private Long id;
    private String orgCode;

    @NotBlank(message = "Mã loại phát sinh không được để trống!")
    @Size(max = 128, message = "Mã loại phát sinh không được vượt quá 128 kí tự!")
    private String entryTypeCode;

    @NotBlank(message = "Tên phát sinh không được để trống!")
    @Size(max = 256, message = "Tên loại phát sinh không được vượt quá 256 kí tự!")
    private String entryName;

    private String description;

    @NotNull(message = "Thứ tự phát sinh không được để trống!")
    private Integer entrySeqNo;

    private String entryCode;

    @NotBlank(message = "Loại sổ kế toán không được để trống!")
    @Size(max = 64, message = "Loại sổ kế toán không được vượt quá 64 kí tự!")
    private String ledgerType;

    @NotBlank(message = "Hướng phát sinh không được để trống!")
    @Size(max = 64, message = "Hướng phát sinh không được vượt quá 64 kí tự!")
    private String entryDir;

    private String acctProcessCode;

    @Valid
    private List<FacCfgAcctEntryDtlDTO> acctEntryDtlDTO;

    private List<Long> deletedIdDtls;

    private String modifiedBy;

}
