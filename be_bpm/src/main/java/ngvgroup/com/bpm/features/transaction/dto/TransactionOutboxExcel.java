package ngvgroup.com.bpm.features.transaction.dto;

import lombok.*;
import ngvgroup.com.bpm.core.contants.ExcelColumns;

import java.sql.Timestamp;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionOutboxExcel {

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
    @ExcelColumn(ExcelColumns.SLA_TASK_DEALINE)
    private Timestamp slaTaskDeadline;
    @ExcelColumn(ExcelColumns.PREV_ACTION_BY)
    private String prevActionBy;
    @ExcelColumn(ExcelColumns.TASK_UPDATE_TIME)
    private Timestamp taskUpdateTime;
    @ExcelColumn(ExcelColumns.SLA_STATUS)
    private String slaStatus;

    // Constructor mới cho JPQL (Bỏ slaStatus)
    @SuppressWarnings("java:S107")
    public TransactionOutboxExcel(String processInstanceCode, Timestamp createDate, String taskDefineName,
                                  String txnContent, Timestamp acceptedDate, Timestamp slaTaskDeadline,
                                  String prevActionBy, Timestamp taskUpdateTime) {
        this.processInstanceCode = processInstanceCode;
        this.createDate = createDate;
        this.taskDefineName = taskDefineName;
        this.txnContent = txnContent;
        this.acceptedDate = acceptedDate;
        this.slaTaskDeadline = slaTaskDeadline;
        this.prevActionBy = prevActionBy;
        this.taskUpdateTime = taskUpdateTime;
    }
}