package ngvgroup.com.bpm.features.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgProcessTypeDTO {
    private String processTypeCode;
    private String processTypeName;
    private String isAccounting;
}
