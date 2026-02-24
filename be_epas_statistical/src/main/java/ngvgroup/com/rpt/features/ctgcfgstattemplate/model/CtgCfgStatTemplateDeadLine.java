package ngvgroup.com.rpt.features.ctgcfgstattemplate.model;

import jakarta.persistence.*;
import lombok.*;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_CFG_STAT_TEMPLATE_DEADLINE")
public class CtgCfgStatTemplateDeadLine extends BaseEntity {
    @Column(name = "ORG_CODE",length = 64, nullable = false)
    private String orgCode;
    @Column(name = "TEMPLATE_CODE",length = 128, nullable = false)
    private String templateCode;
    @Column(name = "FREQUENCY",length = 64, nullable = false)
    private String frequency;
    @Column(name = "DEADLINE_TYPE", length = 64, nullable = false)
    private String deadlineType;
    @Column(name = "DEADLINE_VALUE", length = 64)
    private String deadlineValue;
    @Column(name = "DEADLINE_DAY_TIME", length = 64)
    private String deadlineDayTime;
    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";
    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}
