package ngvgroup.com.rpt.features.ctgcfgstatrulemapping.model;

import jakarta.persistence.*;
import lombok.*;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;


@Entity
@Table(name = "CTG_CFG_STAT_RULE_DEFINE_KPI")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatRuleDefineKpi extends BaseEntity {
    @Column(name = "RULE_CODE", length = 64)
    private String ruleCode;
    // Tham chiếu: CTG_CFG_STAT_RULE_DEFINE.RULE_CODE

    @Column(name = "TEMPLATE_CODE", length = 128)
    private String templateCode;
    // Tham chiếu: CTG_CFG_STAT_TEMPLATE.TEMPLATE_CODE

    @Column(name = "KPI_CODE", length = 32)
    private String kpiCode;
    // Tham chiếu: CTG_CFG_STAT_KPI.KPI_CODE
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
