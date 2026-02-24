package ngvgroup.com.bpm.features.transaction.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor; // Nên thêm NoArgs
import lombok.Setter;
import ngvgroup.com.bpm.core.contants.ExcelColumns;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInboxExcel {
    @ExcelColumn(ExcelColumns.PROCESS_INSTANCE_CODE)
    private String processInstanceCode;
    @ExcelColumn(ExcelColumns.CREATE_DATE)
    private Timestamp createDate;
    @ExcelColumn(ExcelColumns.TASK_DEFINE_NAME)
    private String taskDefineName;
    @ExcelColumn(ExcelColumns.TXN_CONTENT)
    private String txnContent;
    @ExcelColumn(ExcelColumns.ACCEPTED_DATE)
    private Timestamp acceptedDate;
    @ExcelColumn(ExcelColumns.SLA_MAX_DURATION)
    private Long slaMaxDuration;
    @ExcelColumn(ExcelColumns.SLA_TASK_DEALINE)
    private Timestamp slaTaskDeadline;
    @ExcelColumn(ExcelColumns.PREV_ACTION_BY)
    private String prevActionBy;
    @ExcelColumn(ExcelColumns.ACCEPTED_BY)
    private String acceptedBy;
    @ExcelColumn(ExcelColumns.SLA_STATUS)
    private String slaStatus;
    
    private String slaResult;
    private String slaWarningType;
    private Long slaWarningDuration;
    private Long slaWarningPercent;

    private String orgName;

    // Constructor cho JPQL (Bỏ slaStatus)
    @SuppressWarnings("java:S107")
    public TransactionInboxExcel(String processInstanceCode, Timestamp createDate, String taskDefineName,
                                 String txnContent, Timestamp acceptedDate, Long slaMaxDuration,
                                 Timestamp slaTaskDeadline, String prevActionBy, String acceptedBy,
                                 String slaResult, String slaWarningType, Long slaWarningDuration,
                                 Long slaWarningPercent, String orgName) {
        this.processInstanceCode = processInstanceCode;
        this.createDate = createDate;
        this.taskDefineName = taskDefineName;
        this.txnContent = txnContent;
        this.acceptedDate = acceptedDate;
        this.slaMaxDuration = slaMaxDuration;
        this.slaTaskDeadline = slaTaskDeadline;
        this.prevActionBy = prevActionBy;
        this.acceptedBy = acceptedBy;
        this.slaResult = slaResult;
        this.slaWarningType = slaWarningType;
        this.slaWarningDuration = slaWarningDuration;
        this.slaWarningPercent = slaWarningPercent;
        this.orgName = orgName;
    }
}