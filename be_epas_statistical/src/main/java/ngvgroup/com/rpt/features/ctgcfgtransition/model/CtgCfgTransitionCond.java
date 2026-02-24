package ngvgroup.com.rpt.features.ctgcfgtransition.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "CTG_CFG_TRANSITION_COND")
@Getter
@Setter
public class CtgCfgTransitionCond extends BaseEntity {

    @NotBlank
    @Size(max = 64)
    @Column(name = "TRANSITION_CODE", nullable = false, length = 64)
    private String transitionCode;

    @NotBlank
    @Size(max = 64)
    @Column(name = "CONDITION_TYPE", nullable = false, length = 64)
    private String conditionType;

    @Column(name = "CONDITION_NO")
    private Integer conditionNo;

    @Column(name = "IS_MANDATORY", nullable = false)
    private Integer isMandatory = 0;

    @NotBlank
    @Size(max = 64)
    @Column(name = "ENTITY_SCOPE", nullable = false, length = 64)
    private String entityScope;

    @Size(max = 512)
    @Column(name = "ERROR_MESSAGE", length = 512)
    private String errorMessage;

    @Size(max = 2000)
    @Column(name = "EXPRESSION_SQL", length = 4000)
    private String expressionSql;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
