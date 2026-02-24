package ngvgroup.com.bpm.features.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgComCommonDTO {
    private Long id;
    private String description;
    private String commonTypeCode;
    private String commonTypeName;
    private String commonCode;
    private String commonName;
    private String commonValue;
    private String parentCode;
    private String sysMapCode;
    private Long sortNumber;
    private String controlType;
}