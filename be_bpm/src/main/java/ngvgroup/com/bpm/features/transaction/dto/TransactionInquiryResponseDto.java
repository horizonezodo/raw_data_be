package ngvgroup.com.bpm.features.transaction.dto;

import lombok.*;
import java.sql.Timestamp;
import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import ngvgroup.com.bpm.core.contants.ExcelColumns;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInquiryResponseDto {

    @ExcelColumn(ExcelColumns.PROCESS_INSTANCE_CODE)
    private String processInstanceCode;
    @ExcelColumn(ExcelColumns.CREATE_DATE)
    private Timestamp createdDate;
    @ExcelColumn(ExcelColumns.PROCESS_TYPE_NAME)
    private String processTypeName;
    @ExcelColumn(ExcelColumns.TXN_CONTENT_DISPLAY)
    private String txnContent;
    @ExcelColumn(ExcelColumns.BUSINESS_STATUS)
    private String businessStatus;
    @ExcelColumn(ExcelColumns.SLA_TASK_DEALINE)
    private Timestamp slaProcessDeadline;
    @ExcelColumn(ExcelColumns.TASK_UPDATE_TIME)
    private Timestamp modifiedDate;
    @ExcelColumn(ExcelColumns.CREATED_BY)
    private String createdBy;
    
    @ExcelColumn(ExcelColumns.SLA_STATUS)
    private String slaStatus; // Sẽ bỏ qua trong constructor mới
    private String taskId;

    private String slaResult;
    private String slaWarningType;
    private Long slaWarningDuration;
    private Long slaWarningPercent;
    private Long slaMaxDuration;
    private String formKey;

    // Constructor cho JPQL (Bỏ slaStatus)
    @SuppressWarnings("java:S107")
    public TransactionInquiryResponseDto(String processInstanceCode, Timestamp createdDate, String processTypeName,
                                         String txnContent, String businessStatus, Timestamp slaProcessDeadline,
                                         Timestamp modifiedDate, String createdBy, String taskId,
                                         String slaResult, String slaWarningType, Long slaWarningDuration,
                                         Long slaWarningPercent, Long slaMaxDuration, String formKey) {
        this.processInstanceCode = processInstanceCode;
        this.createdDate = createdDate;
        this.processTypeName = processTypeName;
        this.txnContent = txnContent;
        this.businessStatus = businessStatus;
        this.slaProcessDeadline = slaProcessDeadline;
        this.modifiedDate = modifiedDate;
        this.createdBy = createdBy;
        this.taskId = taskId;
        this.slaResult = slaResult;
        this.slaWarningType = slaWarningType;
        this.slaWarningDuration = slaWarningDuration;
        this.slaWarningPercent = slaWarningPercent;
        this.slaMaxDuration = slaMaxDuration;
        this.formKey = formKey;
    }
}