package ngvgroup.com.rpt.features.transactionreport.model;

import jakarta.persistence.*;
import lombok.*;


import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "CTG_CFG_STAT_REGULATORY_WF")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CtgCfgStatRegulatoryWf extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "REGULATORY_TYPE_CODE", nullable = false, length = 64)
    private String regulatoryTypeCode;

    @Column(name = "REGULATORY_TYPE_NAME", nullable = false, length = 128)
    private String regulatoryTypeName;

    @Column(name = "WORKFLOW_CODE", nullable = false, length = 64)
    private String workflowCode;

    @Column(name = "VERSION_NO")
    private Integer versionNo;

    @Column(name = "SORT_NUMBER")
    private Long sortNumber;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
