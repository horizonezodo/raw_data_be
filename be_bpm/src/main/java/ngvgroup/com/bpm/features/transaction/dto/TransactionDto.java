package ngvgroup.com.bpm.features.transaction.dto;

import lombok.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private String processInstanceCode;
    private Timestamp createDate;
    private String taskDefineName;
    private String txnContent;
    private Timestamp acceptedDate;
    private Long slaMaxDuration;
    private Timestamp slaTaskDeadline;
    private String prevActionBy;
    private String acceptedBy;
    private Timestamp taskUpdateTime;
    private String slaResult;
    private String slaWarningType;
    private Long slaWarningDuration;
    private Long slaWarningPercent;

    private String slaStatus;
    private String userId;
    private String userName;
    private String formKey;
    private String taskId;
    private String formAction;
    private Long progressPercent;
    private String orgName;
    private String pathComplete;

    // Constructor dùng cho JPQL Query (bỏ các trường gán '', 0L)
    @SuppressWarnings("java:S107") // Suppress Warning: Methods should not have too many parameters
    public TransactionDto(String processInstanceCode, Timestamp createDate, String taskDefineName,
                          String txnContent, Timestamp acceptedDate, Long slaMaxDuration,
                          Timestamp slaTaskDeadline, String prevActionBy, String acceptedBy,
                          Timestamp taskUpdateTime, String slaResult, String slaWarningType,
                          Long slaWarningDuration, Long slaWarningPercent,
                          String formKey, String taskId, String formAction, String orgName, String pathComplete) {
        this.processInstanceCode = processInstanceCode;
        this.createDate = createDate;
        this.taskDefineName = taskDefineName;
        this.txnContent = txnContent;
        this.acceptedDate = acceptedDate;
        this.slaMaxDuration = slaMaxDuration;
        this.slaTaskDeadline = slaTaskDeadline;
        this.prevActionBy = prevActionBy;
        this.acceptedBy = acceptedBy;
        this.taskUpdateTime = taskUpdateTime;
        this.slaResult = slaResult;
        this.slaWarningType = slaWarningType;
        this.slaWarningDuration = slaWarningDuration;
        this.slaWarningPercent = slaWarningPercent;
        this.formKey = formKey;
        this.taskId = taskId;
        this.formAction = formAction;
        this.orgName = orgName;
        this.pathComplete = pathComplete;
        // Các trường còn lại sẽ null hoặc default
    }
}