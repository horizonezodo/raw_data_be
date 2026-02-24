package ngvgroup.com.bpmn.dto.RuleDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportExcelRuleDto {
    private String ruleCode;
    private String ruleName;
    private String parentCode;
}
