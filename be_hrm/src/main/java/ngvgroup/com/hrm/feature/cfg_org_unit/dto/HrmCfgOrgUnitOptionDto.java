package ngvgroup.com.hrm.feature.cfg_org_unit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrmCfgOrgUnitOptionDto {
    private String orgCode;
    private String orgUnitCode;
    private String orgUnitName;
}
