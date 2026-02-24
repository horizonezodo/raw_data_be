package ngvgroup.com.fac.feature.fac_cfg_voucher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FacCfgVoucherPrintDTO {

    @NotBlank
    private String orgCode;

    @NotBlank
    private String processTypeCode;

    @NotBlank
    private String voucherTypeCode;

    private String printMode;

    @NotBlank
    private String templateCode;

    @NotBlank
    private String templateName;
}
