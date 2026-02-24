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
public class ExportHrmCfgTitleDTO {
    @ExcelColumn("Mã chức danh")
    private String titleCode;
    @ExcelColumn("Tên chức danh")
    private String titleName;
    @ExcelColumn("Chi nhánh")
    private String orgName;
    @ExcelColumn("Trạng thái")
    private Integer isActive;
}
