package ngvgroup.com.fac.feature.fac_inf_acc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacCfgAccPurposeDto {
    private String orgCode;
    private String accPurposeCode;
    private String accPurposeName;
}
