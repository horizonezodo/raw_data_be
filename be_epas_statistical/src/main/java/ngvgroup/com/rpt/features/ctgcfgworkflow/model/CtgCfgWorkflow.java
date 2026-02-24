package ngvgroup.com.rpt.features.ctgcfgworkflow.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "CTG_CFG_WORKFLOW")
@Data
@Getter
@Setter
public class CtgCfgWorkflow extends BaseEntity {
    @Column(name = "WORKFLOW_CODE", nullable = false, length = 64)
    private String workflowCode;

    @Column(name = "WORKFLOW_NAME", nullable = false, length = 128)
    private String workflowName;

    @Column(name = "INITIAL_STATUS_CODE", length = 64)
    private String initialStatusCode;

    @Column(name = "VERSION_NO", precision = 10, scale = 2)
    private BigDecimal versionNo;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}
