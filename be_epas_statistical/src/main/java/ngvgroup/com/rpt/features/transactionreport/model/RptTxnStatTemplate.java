package ngvgroup.com.rpt.features.transactionreport.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "RPT_TXN_STAT_TEMPLATE")
@Getter
@Setter
public class RptTxnStatTemplate extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "STAT_INSTANCE_CODE", nullable = false, length = 64)
    private String statInstanceCode;

    @Column(name = "PREV_STAT_INSTANCE_CODE", length = 64)
    private String prevStatInstanceCode;

    @Column(name = "AGGREGATION_RUN_NO", nullable = false)
    private Integer aggregationRunNo;

    @Column(name = "REV_NO", nullable = false)
    private Integer revNo;

    @Column(name = "TXN_DATE", nullable = false)
    private Date txnDate;

    @Column(name = "MAKER_USER_CODE", nullable = false, length = 64)
    private String makerUserCode;

    @Column(name = "MAKER_USER_NAME", nullable = false, length = 128)
    private String makerUserName;

    @Column(name = "WORKFLOW_CODE", nullable = false, length = 64)
    private String workflowCode;

    @Column(name = "TEMPLATE_CODE", nullable = false, length = 128)
    private String templateCode;

    @Column(name = "TEMPLATE_NAME", nullable = false, length = 256)
    private String templateName;

    @Column(name = "REPORT_PERIOD", nullable = false, length = 32)
    private String reportPeriod;

    @Column(name = "REPORT_DATA_DATE", nullable = false)
    private Date reportDataDate;

    @Column(name = "CURRENT_STATUS_CODE", nullable = false, length = 64)
    private String currentStatusCode;

    @Column(name = "CURRENT_STATUS_NAME", nullable = false, length = 128)
    private String currentStatusName;

    @Column(name = "LAST_TRANSITION_CODE", nullable = false, length = 64)
    private String lastTransitionCode;

    @Column(name = "START_AT", nullable = false)
    private Timestamp startAt;

    @Column(name = "SLA_DUE_AT", nullable = false)
    private Timestamp slaDueAt;

    @Column(name = "SLA_ELAPSED_TIME", nullable = false)
    private Integer slaElapsedTime;

    @Column(name = "IS_VOID", nullable = false)
    private Integer isVoid;

    @Column(name = "VOID_REASON", length = 512)
    private String voidReason;

    @Column(name = "SLA_RESULT", nullable = false, length = 32)
    private String slaResult;

    @Column(name = "REPORT_DUE_TIME", nullable = false)
    private Timestamp reportDueTime;

    @Column(name = "WARNING_SENT_AT")
    private Timestamp warningSentAt;

    @Column(name = "ESCALATED_AT")
    private Timestamp escalatedAt;

    @Column(name = "IS_ESCALATED")
    private Integer isEscalated;

    @Column(name = "SEND_COUNT")
    private Integer sendCount;

    @Column(name = "EXPORT_COUNT")
    private Integer exportCount;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}

