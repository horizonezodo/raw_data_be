package ngvgroup.com.rpt.features.ctgcfgstatus.model;

import jakarta.persistence.*;
import lombok.Data;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.util.Date;

@Entity
@Table(name = "CTG_CFG_STATUS_SLA")
@Data
public class CtgCfgStatusSla extends BaseEntity {
    @Column(name = "STATUS_CODE", nullable = false, length = 64)
    private String statusCode; // CTG_CFG_STATUS.STATUS_CODE

    @Column(name = "DURATION_TIME", nullable = false)
    private Integer durationTime; // Number(4)

    @Column(name = "GRACE_TIME", nullable = false)
    private Integer graceTime; // Number(4)

    @Column(name = "USE_BUSINESS_TIME", nullable = false)
    private Integer useBusinessTime; // 1: business hours, 0: 24/7

    @Column(name = "WARRNING_BEFORE_TIME") // chính tả theo DB
    private Integer warningBeforeTime;

    @Column(name = "ESCALATE_AFTER_TIME")
    private Integer escalateAfterTime;

    @Column(name = "WARRNING_SQL", length = 4000)
    private String warningSql;

    @Column(name = "ESCALATE_SQL", length = 4000)
    private String escalateSql;

    @Column(name = "IS_ENABLE")
    private Integer isEnable; // 1/0

    @Column(name = "TIME_UNIT", length = 32)
    private String timeUnit; // ví dụ: MINUTE/HOUR/DAY

    @Column(name = "EFFECTIVE_DATE")
    @Temporal(TemporalType.DATE)
    private Date effectiveDate;

    @Column(name = "EXPIRY_DATE")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}