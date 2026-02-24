package ngvgroup.com.rpt.features.ctgcfgstat.model;

import jakarta.persistence.*;
import lombok.*;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "CTG_CFG_STAT_CODE_MAP")
public class CtgCfgStatCodeMap extends BaseEntity {
    @Column(name = "STAT_REGULATORY_CODE", length = 64)
    private String statRegulatoryCode;

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "REPORT_MODULE_CODE", length = 64)
    private String reportModuleCode;

    @Column(name = "STAT_TYPE_CODE", length = 64)
    private String statTypeCode;

    @Column(name = "STAT_INTERNAL_CODE", length = 64)
    private String statInternalCode;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
