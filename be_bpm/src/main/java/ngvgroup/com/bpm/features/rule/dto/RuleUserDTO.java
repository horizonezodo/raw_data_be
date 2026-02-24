package ngvgroup.com.bpm.features.rule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RuleUserDTO {
    private Long id;
    private String ruleCode;
    private String userId;
    private String orgCode;
}
