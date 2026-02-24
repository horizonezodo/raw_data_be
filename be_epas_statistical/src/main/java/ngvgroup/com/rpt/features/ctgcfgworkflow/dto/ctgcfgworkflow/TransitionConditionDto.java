package ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransitionConditionDto {
    private Long id;
    private String transitionCode;
    private String conditionType;
    private String conditionTypeName; // Tên loại điều kiện
    private Integer conditionNo;
    private Integer isMandatory;
    private String entityScope;
    private String errorMessage;
    private String expressionSql;
}
