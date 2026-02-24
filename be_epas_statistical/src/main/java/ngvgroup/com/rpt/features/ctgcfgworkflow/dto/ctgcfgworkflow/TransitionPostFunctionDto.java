package ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransitionPostFunctionDto {
    private Long id;
    private String transitionCode;
    private String postFunctionType;
    private String postFunctionTypeName; // Tên loại hậu xử lý
    private Integer postFunctionNo;
    private Integer isAsync;
    private String expressionSql;
}
