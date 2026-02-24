package ngvgroup.com.bpm.core.base.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Entity
@Table(name = "BPM_TXN_TASK_INBOX")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BpmTxnTaskInbox extends BaseEntity {

    @Column(name = "BUSINESS_STATUS", nullable = false, length = 32)
    private String businessStatus;

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "ORG_NAME", length = 256)
    private String orgName;

    @Column(name = "TXN_DATE", nullable = false)
    private Timestamp txnDate;

    @Column(name = "PROCESS_TYPE_CODE", nullable = false, length = 128)
    private String processTypeCode;

    @Column(name = "PROCESS_INSTANCE_CODE", nullable = false, length = 128)
    private String processInstanceCode;

    @Column(name = "TASK_ID", nullable = false, length = 128)
    private String taskId;

    @Column(name = "CUSTOMER_CODE", length = 128)
    private String customerCode;

    @Column(name = "CUSTOMER_NAME", length = 256)
    private String customerName;

    @Column(name = "TASK_DEFINE_CODE", length = 128)
    private String taskDefineCode;

    @Column(name = "TASK_DEFINE_NAME", length = 256)
    private String taskDefineName;

    @Column(name = "TASK_START_TIME")
    private Timestamp taskStartTime;

    @Column(name = "TASK_UPDATE_TIME")
    private Timestamp taskUpdateTime;

    @Column(name = "RULE_CODE", length = 64)
    private String ruleCode;

    @Column(name = "ASSIGN_TO", length = 128)
    private String assignTo;

    @Column(name = "PREV_ACTION_BY", length = 32)
    private String prevActionBy;

    @Column(name = "ACCEPTED_BY", length = 32)
    private String acceptedBy;

    @Column(name = "ACCEPTED_DATE")
    private Timestamp acceptedDate;

    @Column(name = "SLA_MAX_DURATION", precision = 22)
    private Long slaMaxDuration;

    @Column(name = "SLA_TASK_DEADLINE")
    private Timestamp slaTaskDeadline;

    @Column(name = "SLA_WARNING_TYPE", length = 32)
    private String slaWarningType;

    @Column(name = "SLA_WARNING_DURATION", precision = 22)
    private Long slaWarningDuration;

    @Column(name = "SLA_WARNING_PERCENT", precision = 22)
    private Long slaWarningPercent;

    @Column(name = "SLA_RESULT", length = 32)
    private String slaResult;

    @Column(name = "SLA_EVALUATED_AT")
    private Timestamp slaEvaluatedAt;

    @Column(name = "IS_SUSPEND", precision = 1)
    private Integer isSuspend;

    @Column(name = "ADDL_STR_FLD1", length = 512)
    private String addlStrFld1;

    @Column(name = "ADDL_STR_FLD2", length = 512)
    private String addlStrFld2;

    @Column(name = "ADDL_STR_FLD3", length = 512)
    private String addlStrFld3;

    @Column(name = "ADDL_STR_FLD4", length = 512)
    private String addlStrFld4;

    @Column(name = "ADDL_NUM_FLD1", precision = 22)
    private Long addlNumFld1;

    @Column(name = "ADDL_NUM_FLD2", precision = 22)
    private Long addlNumFld2;

    @Column(name = "ADDL_NUM_FLD3", precision = 22)
    private Long addlNumFld3;

    @Column(name = "FORM_KEY", length = 225)
    private String formKey;

    @Column(name = "PATH_COMPLETE", length = 225)
    private String pathComplete;

    @Column(name = "FORM_ACTION", length = 225)
    private String formAction;

}
