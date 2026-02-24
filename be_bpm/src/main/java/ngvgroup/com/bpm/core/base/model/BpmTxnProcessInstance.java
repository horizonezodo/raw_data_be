package ngvgroup.com.bpm.core.base.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "BPM_TXN_PROCESS_INSTANCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BpmTxnProcessInstance extends BaseEntity {
    @Column(name = "BUSINESS_STATUS", nullable = false, length = 32)
    private String businessStatus;

    @Column(name = "TXN_DATE", nullable = false)
    private Timestamp txnDate;

    @Column(name = "PROCESS_INSTANCE_CODE", nullable = false, length = 128)
    private String processInstanceCode;

    @Column(name = "PROCESS_TYPE_CODE", nullable = false, length = 128)
    private String processTypeCode;

    @Column(name = "PROCESS_TYPE_NAME", nullable = false, length = 256)
    private String processTypeName;

    @Column(name = "TXN_CONTENT", nullable = false, length = 4000)
    private String txnContent;

    @Column(name = "CUSTOMER_CODE", nullable = false, length = 128)
    private String customerCode;

    @Column(name = "CUSTOMER_NAME", length = 256)
    private String customerName;

    @Column(name = "ORG_CODE", nullable = false, length = 64)
    private String orgCode;

    @Column(name = "PARENT_CODE", length = 128)
    private String parentCode;

    @Column(name = "SLA_PROCESS_CODE", length = 128)
    private String slaProcessCode;

    @Column(name = "SLA_TYPE", length = 32)
    private String slaType;

    @Column(name = "SLA_MAX_DURATION", precision = 22)
    private Long slaMaxDuration;

    @Column(name = "SLA_WARNING_TYPE", length = 32)
    private String slaWarningType;

    @Column(name = "SLA_WARNING_DURATION", precision = 22)
    private Long slaWarningDuration;

    @Column(name = "SLA_WARNING_PERCENT", precision = 22)
    private Long slaWarningPercent;

    @Column(name = "SLA_PROCESS_DEADLINE")
    private Timestamp slaProcessDeadline;

    @Column(name = "SLA_RESULT", length = 32)
    private String slaResult;

    @Column(name = "SLA_EVALUATED_AT")
    private Timestamp slaEvaluatedAt;

    @Column(name = "IS_SUSPEND", precision = 1)
    private Integer isSuspend;
}
