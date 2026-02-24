package ngvgroup.com.hrm.feature.cfg_org_unit.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HrmCfgOrgUnitSearch {
    private Long id;
    @ExcelColumn("Mã phòng ban")
    private String orgUnitCode;
    @ExcelColumn("Tên phòng ban")
    private String orgUnitName;
    @ExcelColumn("Mã cấp cha")
    private String parentCode;
    @ExcelColumn("Cấp bậc tổ chức")
    private String orgLevelCode;
    @ExcelColumn("Loại đơn vị")
    private String unitTypeCode;
}
