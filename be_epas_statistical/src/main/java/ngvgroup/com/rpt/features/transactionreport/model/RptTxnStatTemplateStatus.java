package ngvgroup.com.rpt.features.transactionreport.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import java.sql.Timestamp;

@Entity
@Table(name = "RPT_TXN_STAT_TEMPLATE_STATUS")
@Getter
@Setter
public class RptTxnStatTemplateStatus extends BaseEntity {

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "STAT_INSTANCE_CODE", nullable = false, length = 64)
    private String statInstanceCode;

    @Column(name = "TEMPLATE_CODE", nullable = false, length = 128)
    private String templateCode;

    @Column(name = "TRANSITION_COMMENT", length = 4000)
    private String transitionComment;

    @Column(name = "TRANSITION_AT", nullable = false)
    private Timestamp transitionAt;

    @Column(name = "TRANSITION_CODE", nullable = false, length = 64)
    private String transitionCode;

    @Column(name = "TRANSITION_NAME", nullable = false, length = 128)
    private String transitionName;

    @Column(name = "STATUS_CODE", nullable = false, length = 64)
    private String statusCode;

    @Column(name = "STATUS_NAME", nullable = false, length = 128)
    private String statusName;

    @Column(name = "TXN_USER_ID", nullable = false, length = 128)
    private String txnUserId;

    @Column(name = "TXN_USER_NAME", nullable = false, length = 256)
    private String txnUserName;

    @Column(name = "SLA_DUE_AT", nullable = false)
    private Timestamp slaDueAt;

    @Column(name = "SLA_ACTUAL_AT")
    private Timestamp slaActualAt;

    @Column(name = "SLA_ELAPSED_TIME")
    private Integer slaElapsedTime;

    @Column(name = "SLA_STATUS", length = 64)
    private String slaStatus;

    @Column(name = "WARNING_SENT_AT")
    private Timestamp warningSentAt;

    @Column(name = "ESCALATED_AT")
    private Timestamp escalatedAt;

    @Column(name = "IS_ESCALATED")
    private Integer isEscalated;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;
}
