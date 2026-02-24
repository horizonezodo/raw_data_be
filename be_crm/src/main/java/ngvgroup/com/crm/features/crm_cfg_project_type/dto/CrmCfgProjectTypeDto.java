package ngvgroup.com.crm.features.crm_cfg_project_type.dto;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrmCfgProjectTypeDto {
    private Long id;

    @ExcelColumn("Mã loại dự án")
    private String projectTypeCode;

    @ExcelColumn("Tên loại dự án")
    private String projectTypeName;

    private String orgCode;

    @ExcelColumn("Mô tả")
    private String description;

}