package ngvgroup.com.rpt.features.ctgcfgai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = " CTG_CFG_AI_TOOL_TYPE")
public class CtgCfgAiToolType extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;
    @Column(name = "TOOL_AI_TYPE_CODE", length = 64, nullable = false)
    private String toolAiTypeCode;
    @Column(name = "TOOL_AI_TYPE_NAME", length = 128, nullable = false)
    private String toolAiTypeName;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
