package ngvgroup.com.fac.feature.fac_cfg_acct_entry.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacCfgAcctProcessDTO {
    private Long id;
    private String modifiedBy;
    private String orgCode;
    private String acctProcessCode;

    @NotBlank(message = "Mã loại quy trình không được để trống!")
    @Size(max = 128, message = "Mã loại quy trình không được vượt quá 128 kí tự!")
    private String processTypeCode;

    @NotBlank(message = "Tên loại quy trình không được để trống!")
    @Size(max = 256, message = "Tên loại quy trình không được vượt quá 256 kí tự!")
    private String processTypeName;

    @NotBlank(message = "Phân hệ nghiệp vụ không được để trống!")
    @Size(max = 64, message = "Phân hệ nghiệp vụ không được vượt quá 64 ký tự!")
    private String moduleCode;

    private Boolean isApplyAll;

    private Integer isActive;

    @Valid
    private List<FacCfgAcctEntryDTO> entryDTO;

    private List<Long> deletedEntryIds;
}