package ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto;

import lombok.*;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;

import ngvgroup.com.rpt.core.constant.ExcelColumns;

@Getter
@Setter
public class ResponseSearchStatRuleDefineDto {
    private Long id;
    @ExcelColumn(ExcelColumns.RLD_RULE_CODE)
    private String ruleCode;
    @ExcelColumn(ExcelColumns.RLD_RULE_NAME)
    private String ruleName;
    @ExcelColumn(ExcelColumns.RLD_RULE_TYPE_NAME)
    private String ruleTypeName;
    @ExcelColumn(ExcelColumns.RLD_DESCRIPTION)
    private String description;


    public ResponseSearchStatRuleDefineDto(Long id, String ruleCode, String ruleName, String ruleTypeName, String description) {
        this.id = id;
        this.ruleCode = ruleCode;
        this.ruleName = ruleName;
        this.ruleTypeName = ruleTypeName;
        this.description = description;
    }
}