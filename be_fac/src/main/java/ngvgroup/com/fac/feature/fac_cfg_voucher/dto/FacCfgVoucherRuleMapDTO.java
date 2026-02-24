package ngvgroup.com.fac.feature.fac_cfg_voucher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FacCfgVoucherRuleMapDTO {

    @NotBlank
    private String orgCode;

    private String priority;

    @NotBlank
    private String processTypeCode;

    @NotBlank
    private String ledgerType;

    @NotBlank
    private String paymentMethod;

    @NotBlank
    private String cashFlowDir;

    @NotBlank
    private String offbsDir;

    @NotBlank
    private String forcedVoucherType;

    @NotBlank
    private String voucherTypeCode;
}
