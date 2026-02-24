package ngvgroup.com.bpm.features.rule.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.bpm.core.contants.ExcelColumns;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RuleExcelDto {
    @ExcelColumn(ExcelColumns.RULE_CODE)
    private String ruleCode;
    @ExcelColumn(ExcelColumns.RULE_NAME)
    private String ruleName;
    @ExcelColumn(ExcelColumns.PARENT_CODE)
    private String parentCode;
}
