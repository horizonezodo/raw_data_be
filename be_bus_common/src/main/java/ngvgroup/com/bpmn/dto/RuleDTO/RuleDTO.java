package ngvgroup.com.bpmn.dto.RuleDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.bpmn.dto.PageDTO.PageableDTO;

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
    private PageableDTO pageable;
    private String filter;
}
