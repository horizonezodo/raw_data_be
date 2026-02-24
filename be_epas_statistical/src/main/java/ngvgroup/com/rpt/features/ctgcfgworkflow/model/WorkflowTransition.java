package ngvgroup.com.rpt.features.ctgcfgworkflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "CTG_CFG_WORKFLOW_TRANSITION")
@Data
@Getter
@Setter
public class WorkflowTransition extends BaseEntity {

    @NotBlank
    @Size(max = 64)
    @Column(name = "WORKFLOW_CODE", nullable = false, length = 64)
    private String workflowCode;

    @NotBlank
    @Size(max = 64)
    @Column(name = "TRANSITION_CODE", nullable = false, length = 64)
    private String transitionCode;

    @NotBlank
    @Size(max = 128)
    @Column(name = "TRANSITION_NAME", nullable = false, length = 128)
    private String transitionName;

    @NotBlank
    @Size(max = 64)
    @Column(name = "FROM_STATUS_CODE", nullable = false, length = 64)
    private String fromStatusCode;

    @NotBlank
    @Size(max = 128)
    @Column(name = "FROM_STATUS_NAME", nullable = false, length = 128)
    private String fromStatusName;

    @NotBlank
    @Size(max = 64)
    @Column(name = "TO_STATUS_CODE", nullable = false, length = 64)
    private String toStatusCode;

    @NotBlank
    @Size(max = 128)
    @Column(name = "TO_STATUS_NAME", nullable = false, length = 128)
    private String toStatusName;

    @Column(name = "IS_GLOBAL", nullable = false)
    private Integer isGlobal = 0;

    @Column(name = "SORT_NUMBER")
    private Integer sortNumber;

    @Column(name = "IS_ALLOW_COMMENT", nullable = false)
    private Integer isAllowComment = 1;

    @Column(name = "IS_ALLOW_ATTACHMENT", nullable = false)
    private Integer isAllowAttachment = 0;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
