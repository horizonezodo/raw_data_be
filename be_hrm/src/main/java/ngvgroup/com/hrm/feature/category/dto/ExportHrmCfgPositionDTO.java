package ngvgroup.com.hrm.feature.category.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportHrmCfgPositionDTO {
    @ExcelColumn("Mã chức vụ")
    private String positionCode;
    @ExcelColumn("Tên chức vụ")
    private String positionName;
    @ExcelColumn("Chức danh")
    private String titleName;
    @ExcelColumn("Cấp tổ chức")
    private String orgLevelCode;
    @ExcelColumn("Cấp quản lý")
    private Integer isManager;
}
