package ngvgroup.com.hrm.feature.hrm_cfg_status.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrmCfgStatusOptionDto {
    private String orgCode;
    private String statusCode;
    private String statusName;
}
