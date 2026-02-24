package ngvgroup.com.rpt.features.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgCommonDto {
    private String commonTypeCode;
    private String commonTypeName;
    private String commonCode;
    private String commonName;
    private String commonValue;
    private boolean isChecked;

    public ComCfgCommonDto(String commonCode, String commonName) {
        this.commonCode = commonCode;
        this.commonName = commonName;
    }
}
