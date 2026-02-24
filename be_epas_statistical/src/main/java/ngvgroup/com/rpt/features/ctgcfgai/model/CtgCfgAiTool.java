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
@Table(name = "CTG_CFG_AI_TOOL")
public class CtgCfgAiTool extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;
    @Column(name = "TOOL_AI_CODE", length = 64, nullable = false)
    private String toolAiCode;
    @Column(name = "TOOL_AI_Name", length = 128, nullable = false)
    private String toolAiName;
    @Column(name = "TOOL_AI_TYPE_CODE", length = 64, nullable = false)
    private String toolAiTypeCode;
    @Column(name = "INPUT_PARAMETER")
    private String inputParameter;
    @Column(name = "TEMPLATE_CONFIG")
    private String templateConfig;
    @Column(name = "OUTPUT_FORMAT ", length = 256)
    private String outputFormat;
    @Column(name = "TIMEOUT_SECOND", length = 3)
    private double timeoutSecond;
    @Column(name = "RETRY_COUNT ", length = 3)
    private int retryCount;
    @Column(name = "REQUIRES_AUTH")
    private boolean requiresAuth;
    @Column(name = "ALLOWED_ROLE")
    private String allowedRole;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
