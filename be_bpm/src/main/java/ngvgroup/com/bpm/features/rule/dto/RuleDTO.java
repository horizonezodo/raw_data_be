package ngvgroup.com.bpm.features.rule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.print.Pageable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RuleDTO {
    private Long id;
    private String ruleCode;
    private String ruleName;
    private String parentCode;
    private String orgCode;
    private List<String> userIds;
    private String userId;
    private List<String> label;
    private Pageable pageable;
    private String filter;
}
