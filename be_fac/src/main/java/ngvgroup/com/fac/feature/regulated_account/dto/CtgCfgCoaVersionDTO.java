package ngvgroup.com.fac.feature.regulated_account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtgCfgCoaVersionDTO {
    private String coaVersionCode;
    private String coaScope;
    private int isDefault;
    private String coaVersionName;
    private String commonValue;

    public CtgCfgCoaVersionDTO(String coaVersionCode, String coaScope, int isDefault, String coaVersionName) {
        this.coaVersionCode = coaVersionCode;
        this.coaScope = coaScope;
        this.isDefault = isDefault;
        this.coaVersionName = coaVersionName;
    }
}
