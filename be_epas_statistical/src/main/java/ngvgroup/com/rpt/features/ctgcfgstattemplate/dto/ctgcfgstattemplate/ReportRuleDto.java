package ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.rpt.core.constant.ExcelColumns;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRuleDto {
    @ExcelColumn(ExcelColumns.RR_CIRCULAR_NAME)
    private String circularName;
    private String circularCode;
    private String templateGroupCode;
    private String templateGroupName;
    @ExcelColumn(ExcelColumns.RR_TEMPLATE_CODE)
    private String templateCode;
    @ExcelColumn(ExcelColumns.RR_TEMPLATE_NAME)
    private String templateName;
    @ExcelColumn(ExcelColumns.RR_FREQUENCY)
    private String commonName;
    private String regulatoryTypeCode;
    @ExcelColumn(ExcelColumns.RR_STATUS_NAME)
    private String statusName;
    @ExcelColumn(ExcelColumns.RLD_DESCRIPTION)
    private String description;

}
