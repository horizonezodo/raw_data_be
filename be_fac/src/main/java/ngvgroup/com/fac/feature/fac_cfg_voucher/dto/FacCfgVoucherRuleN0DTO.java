package ngvgroup.com.fac.feature.fac_cfg_voucher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FacCfgVoucherRuleN0DTO {

    @NotBlank
    private String orgCode;

    @NotBlank
    private String voucherTypeCode;

    @NotBlank
    private String voucherTypeName;

    @NotBlank
    private String periodType;

    @NotBlank
    private String prefix;

    @NotBlank
    private String separator;

    @NotBlank
    private Integer lengthSeq;

    @NotBlank
    private Integer startSeq;

    @NotBlank
    private String formatOrder;
}
