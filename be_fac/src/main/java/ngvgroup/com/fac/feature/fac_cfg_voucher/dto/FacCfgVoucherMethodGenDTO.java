package ngvgroup.com.fac.feature.fac_cfg_voucher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FacCfgVoucherMethodGenDTO  {

    @NotBlank
    private String orgCode;

    @NotBlank
    private String processTypeCode;

    @NotBlank
    private String processTypeName;

    @NotBlank
    private String voucherGenMode;
}
