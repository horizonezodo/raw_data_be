package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ngvgroup.com.rpt.core.constant.ExcelColumns;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgStatTypeListDto {
    private Long id;
    private String recordStatus;
    private Integer isActive;
    private String orgCode;
    @ExcelColumn(ExcelColumns.STAT_TYPE_CODE)
    private String statTypeCode;
    @ExcelColumn(ExcelColumns.STAT_TYPE_NAME)
    private String statTypeName;
    @ExcelColumn(ExcelColumns.REPORT_MODULE_CODE)
    private String reportModuleCode;
    private String expressionSql;
    private Long sortNumber;
    @ExcelColumn(ExcelColumns.REPORT_MODULE_NAME)
    private String reportModuleName;
}
