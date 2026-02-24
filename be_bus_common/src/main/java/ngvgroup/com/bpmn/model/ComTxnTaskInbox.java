package ngvgroup.com.bpmn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_TXN_TASK_INBOX")
public class ComTxnTaskInbox extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "BUSINESS_STATUS", length = 32, nullable = false)
    private String businessStatus;

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "ORG_NAME", length = 256)
    private String orgName;

    @Column(name = "TXN_DATE", nullable = false)
    private Date txnDate;

    @Column(name = "PROCESS_TYPE_CODE", length = 128, nullable = false)
    private String processTypeCode;

    @Column(name = "PROCESS_INSTANCE_CODE", length = 128, nullable = false)
    private String processInstanceCode;

    @Column(name = "TASK_ID", length = 128)
    private String taskId;

    @Column(name = "CUSTOMER_CODE", length = 128)
    private String customerCode;

    @Column(name = "TASK_DEFINE_CODE", length = 128)
    private String taskDefineCode;

    @Column(name = "TASK_DEFINE_NAME", length = 256)
    private String taskDefineName;

    @Column(name = "ACCEPTED_BY", length = 32)
    private String acceptedBy;

    @Column(name = "ACCEPTED_DATE")
    private Timestamp acceptedDate;

    @Column(name = "TASK_START_TIME")
    private Timestamp taskStartTime;

    @Column(name = "TASK_UPDATE_TIME")
    private Timestamp taskUpdateTime;

    @Column(name = "SLA_MAX_DURATION")
    private Double slaMaxDuration;

    @Column(name = "SLA_WARNING_TYPE", length = 64)
    private String slaWarningType;

    @Column(name = "SLA_WARNING_DURATION")
    private Double slaWarningDuration;

    @Column(name = "SLA_WARNING_PERCENT")
    private Double slaWarningPercent;

    @Column(name = "SLA_RESULT", length = 32)
    private String slaResult;

    @Column(name = "SLA_STATUS", length = 64)
    private String slaStatus;

    @Column(name = "RULE_CODE", length = 64)
    private String ruleCode;

    @Column(name = "ASSIGN_TO", length = 512)
    private String assignTo;

    @Column(name = "SLA_EVALUATED_AT")
    private Timestamp slaEvaluatedAt;

    @Column(name = "IS_SUSPEND")
    private int isSuspend;

    @Column(name = "ADDL_STR_FLD1", length = 512)
    private String addlStrFld1;

    @Column(name = "ADDL_STR_FLD2", length = 512)
    private String addlStrFld2;

    @Column(name = "ADDL_STR_FLD3", length = 512)
    private String addlStrFld3;

    @Column(name = "SLA_TASK_DEADLINE")
    private Timestamp slaTaskDeadLine;
}