package ngvgroup.com.rpt.features.transactionreport.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.sql.Timestamp;

@Entity
@Table(name = "RPT_TXN_STAT_KPI_AUDIT")
@Getter
@Setter
public class RptTxnStatKpiAudit extends BaseEntity {
    @Column(name = "STAT_INSTANCE_CODE", nullable = false, length = 64)
    private String statInstanceCode;

    @Column(name = "KPI_CODE", nullable = false, length = 32)
    private String kpiCode;

    @Column(name = "KPI_OLD_VALUE", length = 4000)
    private String kpiOldValue;

    @Column(name = "KPI_NEW_VALUE", length = 4000)
    private String kpiNewValue;

    @Column(name = "EDIT_REASON", length = 4000)
    private String editReason;

    @Column(name = "TXN_USER_ID", nullable = false, length = 128)
    private String txnUserId;

    @Column(name = "TXN_USER_NAME", nullable = false, length = 256)
    private String txnUserName;

    @Column(name = "CHANGED_AT")
    private Timestamp changedAt;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
