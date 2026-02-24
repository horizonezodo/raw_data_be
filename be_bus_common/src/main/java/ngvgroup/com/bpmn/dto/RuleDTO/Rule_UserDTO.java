package ngvgroup.com.bpmn.dto.RuleDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rule_UserDTO {
    private Long id;
    private String ruleCode;
    private String userId;
    private String orgCode;
}
