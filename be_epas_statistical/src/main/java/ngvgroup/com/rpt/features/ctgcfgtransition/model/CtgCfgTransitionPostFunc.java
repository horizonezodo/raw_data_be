package ngvgroup.com.rpt.features.ctgcfgtransition.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "CTG_CFG_TRANSITION_POST_FUNC")
@Getter
@Setter
public class CtgCfgTransitionPostFunc extends BaseEntity {
    @NotBlank
    @Size(max = 64)
    @Column(name = "TRANSITION_CODE", nullable = false, length = 64)
    private String transitionCode;

    @NotBlank
    @Size(max = 64)
    @Column(name = "POST_FUNCTION_TYPE", nullable = false, length = 64)
    private String postFunctionType;

    @Column(name = "POST_FUNCTION_NO")
    private Integer postFunctionNo;

    @Column(name = "IS_ASYNC", nullable = false)
    private Integer isAsync = 1;

    @Size(max = 2000)
    @Column(name = "EXPRESSION_SQL", length = 4000)
    private String expressionSql;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
