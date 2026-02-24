package ngvgroup.com.rpt.features.ctgcfgstatruletype.dto;


import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;

import ngvgroup.com.rpt.core.constant.ExcelColumns;

@Getter
@Setter
public class ResponseSearchStatRuleTypeDto {
    private Long id;
    @ExcelColumn(ExcelColumns.RLT_RULE_TYPE_CODE)
    private String ruleTypeCode;
    @ExcelColumn(ExcelColumns.RLT_RULE_TYPE_NAME)
    private String ruleTypeName;
    @ExcelColumn(ExcelColumns.RLT_DESCRIPTION)
    private String description;

    public ResponseSearchStatRuleTypeDto(Long id , String ruleTypeCode, String ruleTypeName, String description) {
        this.id = id;
        this.ruleTypeCode = ruleTypeCode;
        this.ruleTypeName = ruleTypeName;
        this.description = description;
    }
}
