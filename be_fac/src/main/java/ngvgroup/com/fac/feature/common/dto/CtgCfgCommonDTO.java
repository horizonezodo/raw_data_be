package ngvgroup.com.fac.feature.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgCommonDTO {
    private String commonTypeCode;
    private String commonTypeName;
    private String commonCode;
    private String commonName;
    private String commonValue;
    private String parentCode;
}
