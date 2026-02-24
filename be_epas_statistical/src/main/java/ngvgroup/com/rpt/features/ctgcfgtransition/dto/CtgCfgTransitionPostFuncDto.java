package ngvgroup.com.rpt.features.ctgcfgtransition.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgTransitionPostFuncDto {
    private Long id;
    private String transitionCode;
    private String transitionName;
    private String postFunctionType;
    private String postFunctionTypeName; // Tên loại hậu xử lý
    private Integer postFunctionNo;
    private Integer isAsync;
    private String expressionSql;
    private String recordStatus;
}
