package ngvgroup.com.rpt.features.ctgcfgstatus.model;

import com.ngvgroup.bpm.core.logging.audit.annotation.AuditIgnore;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CTG_CFG_STATUS")
@Getter
@Setter
public class CtgCfgStatus extends BaseEntity {

    

    @Column(name = "SORT_NUMBER")
    private Long sortNumber;

    @Column(name = "STATUS_CODE", nullable = false, length = 64)
    private String statusCode;

    @Column(name = "STATUS_NAME", nullable = false, length = 128)
    private String statusName;

    @Column(name = "STATUS_TYPE_CODE", length = 64)
    private String statusTypeCode;

    @Column(name = "STATUS_TYPE_NAME", length = 128)
    @AuditIgnore
    private String statusTypeName;

    @Column(name = "IS_FINAL")
    private Integer isFinal;

    @Column(name = "IS_AGGREGATION", length = 64)
    private Integer isAggregation;

    @Column(name = "IS_ALLOW_EDIT", length = 64)
    private Integer isAllowEdit;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

    @Column(name = "IS_CHECK")
    private Integer isCheck = 0;

}